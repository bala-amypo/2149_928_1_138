package com.example.demo.service;

import com.example.demo.entity.KeyShareRequest;
import java.util.List;

public interface KeyShareRequestService {
    KeyShareRequest createRequest(KeyShareRequest request);
    KeyShareRequest getRequest(Long id);
    List<KeyShareRequest> getAllRequests();
}
