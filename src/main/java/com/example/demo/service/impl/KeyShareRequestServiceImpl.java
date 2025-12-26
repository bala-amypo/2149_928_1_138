package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.KeyShareRequestService;

import java.util.List;

public class KeyShareRequestServiceImpl implements KeyShareRequestService {

    private final KeyShareRequestRepository repo;
    private final DigitalKeyRepository keyRepo;
    private final GuestRepository guestRepo;

    public KeyShareRequestServiceImpl(KeyShareRequestRepository repo,
                                      DigitalKeyRepository keyRepo,
                                      GuestRepository guestRepo) {
        this.repo = repo;
        this.keyRepo = keyRepo;
        this.guestRepo = guestRepo;
    }

    @Override
    public KeyShareRequest createShareRequest(KeyShareRequest request) {
        if (request.getShareEnd().before(request.getShareStart())) {
            throw new IllegalArgumentException("Share end must be after start");
        }

        if (request.getSharedBy().getId().equals(request.getSharedWith().getId())) {
            throw new IllegalArgumentException("sharedBy and sharedWith cannot be same");
        }

        DigitalKey key = keyRepo.findById(request.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));

        if (!key.getActive()) {
            throw new IllegalStateException("Key inactive");
        }

        guestRepo.findById(request.getSharedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        guestRepo.findById(request.getSharedWith().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        request.setStatus("PENDING");
        return repo.save(request);
    }

    @Override
    public KeyShareRequest updateStatus(Long requestId, String status) {
        KeyShareRequest req = getShareRequestById(requestId);
        req.setStatus(status);
        return repo.save(req);
    }

    @Override
    public KeyShareRequest getShareRequestById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Share request not found " + id));
    }

    @Override
    public List<KeyShareRequest> getRequestsSharedBy(Long guestId) {
        return repo.findBySharedById(guestId);
    }

    @Override
    public List<KeyShareRequest> getRequestsSharedWith(Long guestId) {
        return repo.findBySharedWithId(guestId);
    }
}
