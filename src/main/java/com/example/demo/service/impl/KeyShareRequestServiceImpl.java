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

        if (!request.getShareEnd().isAfter(request.getShareStart())) {
            throw new IllegalArgumentException("share end must be after start");
        }

        if (request.getSharedBy().getId()
                .equals(request.getSharedWith().getId())) {
            throw new IllegalArgumentException("self share not allowed");
        }

        DigitalKey key = keyRepo.findById(request.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("key not found"));

        if (!Boolean.TRUE.equals(key.getActive())) {
            throw new IllegalStateException("key inactive");
        }

        Guest sharedBy = guestRepo.findById(request.getSharedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("guest not found"));

        Guest sharedWith = guestRepo.findById(request.getSharedWith().getId())
                .orElseThrow(() -> new ResourceNotFoundException("guest not found"));

        if (!Boolean.TRUE.equals(sharedWith.getVerified()) ||
            !Boolean.TRUE.equals(sharedWith.getActive())) {
            throw new IllegalStateException("recipient not eligible");
        }

        request.setDigitalKey(key);
        request.setSharedBy(sharedBy);
        request.setSharedWith(sharedWith);
        request.setStatus("PENDING");

        return repo.save(request);
    }

    @Override
    public KeyShareRequest updateStatus(Long requestId, String status) {

        if (!List.of("PENDING", "APPROVED", "REJECTED").contains(status)) {
            throw new IllegalArgumentException("invalid status");
        }

        KeyShareRequest req = getShareRequestById(requestId);

        if ("APPROVED".equals(status)) {
            DigitalKey key = req.getDigitalKey();

            // ✅ FIX: Direct Instant comparison (NO toInstant)
            if (req.getShareStart().isBefore(key.getIssuedAt()) ||
                req.getShareEnd().isAfter(key.getExpiresAt())) {
                throw new IllegalStateException("outside key validity");
            }
        }

        req.setStatus(status);
        return repo.save(req);
    }

    @Override
    public KeyShareRequest getShareRequestById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("share request not found"));
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
