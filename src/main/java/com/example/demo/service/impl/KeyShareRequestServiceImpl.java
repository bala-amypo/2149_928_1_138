package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.Guest;
import com.example.demo.model.KeyShareRequest;
import com.example.demo.model.ShareStatus;
import com.example.demo.repository.KeyShareRequestRepository;
import com.example.demo.service.KeyShareRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyShareRequestServiceImpl implements KeyShareRequestService {

    private final KeyShareRequestRepository repository;

    public KeyShareRequestServiceImpl(KeyShareRequestRepository repository) {
        this.repository = repository;
    }

    // ================= CREATE =================
    @Override
    public KeyShareRequest createShareRequest(KeyShareRequest request) {

        Guest from = request.getSharedBy();
        Guest to = request.getSharedWith();

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Cannot share key with self");
        }

        if (!to.isVerified() || !to.isActive()) {
            throw new IllegalStateException("Recipient not eligible");
        }

        if (request.getShareEnd().isBefore(request.getShareStart())) {
            throw new IllegalArgumentException("Invalid share window");
        }

        DigitalKey key = request.getDigitalKey();
        if (!key.isActive()) {
            throw new IllegalStateException("Key not active");
        }

        // status defaults to PENDING
        return repository.save(request);
    }

    // ================= UPDATE STATUS =================
    @Override
    public KeyShareRequest updateStatus(Long requestId, ShareStatus status) {
        KeyShareRequest request = getShareRequestById(requestId);
        request.setStatus(status);
        return repository.save(request);
    }

    // ================= GET =================
    @Override
    public KeyShareRequest getShareRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Share request not found"));
    }

    @Override
    public List<KeyShareRequest> getRequestsSharedBy(Long guestId) {
        return repository.findBySharedById(guestId);
    }

    @Override
    public List<KeyShareRequest> getRequestsSharedWith(Long guestId) {
        return repository.findBySharedWithId(guestId);
    }
}