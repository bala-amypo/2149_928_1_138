package com.example.demo.service.impl;

import com.example.demo.entity.KeyShareRequest;
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

    public KeyShareRequest createRequest(KeyShareRequest request) {
        return repository.save(request);
    }

    public KeyShareRequest getRequest(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<KeyShareRequest> getAllRequests() {
        return repository.findAll();
    }
}
