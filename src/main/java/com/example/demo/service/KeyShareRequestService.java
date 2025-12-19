package com.example.demo.service;

import com.example.demo.model.KeyShareRequest;
import java.util.List;

public interface KeyShareRequestService {

    /**
     * Create a new key share request
     */
    KeyShareRequest createShareRequest(KeyShareRequest request);

    /**
     * Approve or reject a request
     */
    KeyShareRequest updateStatus(Long id, String status);

    /**
     * Requests received by a guest
     */
    List<KeyShareRequest> receivedRequests(Long guestId);

    /**
     * Requests sent by a guest
     */
    List<KeyShareRequest> sentRequests(Long guestId);
}
