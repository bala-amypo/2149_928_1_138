package com.example.demo.service;

import com.example.demo.model.KeyShareRequest;
import java.util.List;

public interface KeyShareRequestService {
    KeyShareRequest create(KeyShareRequest request);
    List<KeyShareRequest> getAll();
}
