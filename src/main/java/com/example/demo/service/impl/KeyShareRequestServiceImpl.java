package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.Guest;
import com.example.demo.model.KeyShareRequest;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.KeyShareRequestRepository;
import com.example.demo.service.KeyShareRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyShareRequestServiceImpl implements KeyShareRequestService {

    private final KeyShareRequestRepository repo;
    private final DigitalKeyRepository keyRepo;
    private final GuestRepository guestRepo;

    public KeyShareRequestServiceImpl(
            KeyShareRequestRepository repo,
            DigitalKeyRepository keyRepo,
            GuestRepository guestRepo) {
        this.repo = repo;
        this.keyRepo = keyRepo;
        this.guestRepo = guestRepo;
    }

    @Override
    public KeyShareRequest createShareRequest(KeyShareRequest request) {

        // ✅ Share window validation
        if (request.getShareEnd().before(request.getShareStart())) {
            throw new IllegalArgumentException("Share end must be after start");
        }

        // ✅ Self-share validation
        if (request.getSharedBy().getId()
                .equals(request.getSharedWith().getId())) {
            throw new IllegalArgumentException("sharedBy and sharedWith cannot be same");
        }

        // ==================================================
        // 🔥 FIX: FETCH FULL ENTITIES AND ATTACH THEM
        // ==================================================

        DigitalKey key = keyRepo.findById(request.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));

        if (!Boolean.TRUE.equals(key.getActive())) {
            throw new IllegalStateException("Key inactive");
        }

        Guest sharedBy = guestRepo.findById(request.getSharedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        Guest sharedWith = guestRepo.findById(request.getSharedWith().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        // ✅ Attach fully-loaded entities
        request.setDigitalKey(key);
        request.setSharedBy(sharedBy);
        request.setSharedWith(sharedWith);
        request.setStatus("PENDING");

        return repo.save(request);
    }

    @Override
    public KeyShareRequest updateStatus(Long requestId, String status) {
        KeyShareRequest req = getShareRequestById(requestId);
        req.setStatus(status);
        return repo.save(req);
    }
}