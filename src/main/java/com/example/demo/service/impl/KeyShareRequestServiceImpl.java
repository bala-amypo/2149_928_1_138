// src/main/java/com/example/demo/service/impl/KeyShareRequestServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.KeyShareRequest;
import com.example.demo.exception.ResourceNotFoundException;
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

    @Override
    public KeyShareRequest create(KeyShareRequest request) {
        return repository.save(request);
    }

    @Override
    public KeyShareRequest approve(Long id) {
        KeyShareRequest req = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        req.setApproved(true);
        return repository.save(req);
    }

    @Override
    public List<KeyShareRequest> getAll() {
        return repository.findAll();
    }
}
