package com.example.demo.service;

import com.example.demo.model.KeyShareRequest;
import com.example.demo.model.ShareStatus;

import java.util.List;

public interface KeyShareRequestService {

    KeyShareRequest createShareRequest(KeyShareRequest request);

    KeyShareRequest updateStatus(Long requestId, ShareStatus status);

    KeyShareRequest getShareRequestById(Long id);

    List<KeyShareRequest> getRequestsSharedBy(Long guestId);

    List<KeyShareRequest> getRequestsSharedWith(Long guestId);
}
