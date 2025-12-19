// src/main/java/com/example/demo/service/KeyShareRequestService.java
package com.example.demo.service;

import com.example.demo.entity.KeyShareRequest;
import java.util.List;

public interface KeyShareRequestService {
    KeyShareRequest create(KeyShareRequest request);
    KeyShareRequest approve(Long id);
    List<KeyShareRequest> getAll();
}
