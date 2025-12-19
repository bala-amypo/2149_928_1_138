package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.DigitalKey;

public interface DigitalKeyService {

    DigitalKey create(DigitalKey digitalKey);

    DigitalKey getById(Long id);

    List<DigitalKey> getAll();

    DigitalKey update(Long id, DigitalKey digitalKey);

    void delete(Long id);
}
