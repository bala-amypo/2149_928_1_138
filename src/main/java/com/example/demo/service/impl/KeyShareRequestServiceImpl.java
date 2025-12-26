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

import java.time.Instant;
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

        if (request == null) {
            throw new IllegalArgumentException("Share request cannot be null");
        }

        if (request.getShareStart() == null || request.getShareEnd() == null) {
            throw new IllegalArgumentException("Share start and end are required");
        }

        if (!request.getShareEnd().isAfter(request.getShareStart())) {
            throw new IllegalArgumentException("Share end must be after start");
        }

        if (request.getShareEnd().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Share end cannot be in the past");
        }

        if (request.getSharedBy() == null || request.getSharedWith() == null) {
            throw new IllegalArgumentException("SharedBy and SharedWith are required");
        }

        if (request.getSharedBy().getId()
                .equals(request.getSharedWith().getId())) {
            throw new IllegalArgumentException(
                    "sharedBy and sharedWith cannot be same");
        }

        DigitalKey key = keyRepo.findById(request.getDigitalKey().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Key not found with id: " +
                                        request.getDigitalKey().getId()));

        if (!Boolean.TRUE.equals(key.getActive())) {
            throw new IllegalStateException("Key is inactive");
        }

        Guest sharedBy = guestRepo.findById(request.getSharedBy().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: " +
                                        request.getSharedBy().getId()));

        Guest sharedWith = guestRepo.findById(request.getSharedWith().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: " +
                                        request.getSharedWith().getId()));

        if (!Boolean.TRUE.equals(sharedWith.getVerified()) ||
            !Boolean.TRUE.equals(sharedWith.getActive())) {
            throw new IllegalStateException("Recipient not eligible");
        }

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

    @Override
    public KeyShareRequest getShareRequestById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Share request not found with id: " + id));
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
