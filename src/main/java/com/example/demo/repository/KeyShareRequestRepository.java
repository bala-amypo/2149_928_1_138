package com.example.demo.repository;

import com.example.demo.entity.KeyShareRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KeyShareRequestRepository {

    private final List<KeyShareRequest> requests = new ArrayList<>();

    public KeyShareRequest save(KeyShareRequest request) {
        requests.add(request);
        return request;
    }

    public List<KeyShareRequest> findAll() {
        return requests;
    }
}
