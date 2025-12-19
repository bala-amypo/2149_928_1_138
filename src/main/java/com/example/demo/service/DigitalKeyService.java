package com.example.demo.service;

import com.example.demo.model.DigitalKey;
import java.util.List;

public interface DigitalKeyService {
    DigitalKey create(DigitalKey key);
    List<DigitalKey> getAll();
}
