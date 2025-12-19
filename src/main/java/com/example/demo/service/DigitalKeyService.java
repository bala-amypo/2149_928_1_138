package com.example.demo.service;

import com.example.demo.entity.DigitalKey;
import java.util.List;

public interface DigitalKeyService {
    DigitalKey createKey(DigitalKey key);
    DigitalKey getKey(Long id);
    List<DigitalKey> getAllKeys();
    void deactivateKey(Long id);
}
