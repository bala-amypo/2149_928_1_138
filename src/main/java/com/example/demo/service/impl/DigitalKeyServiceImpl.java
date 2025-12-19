package com.example.demo.service.impl;

import com.example.demo.entity.DigitalKey;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.service.DigitalKeyService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository repository;

    public DigitalKeyServiceImpl(DigitalKeyRepository repository) {
        this.repository = repository;
    }

    public DigitalKey createKey(DigitalKey key) {
        return repository.save(key);
    }

    public DigitalKey getKey(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<DigitalKey> getAllKeys() {
        return repository.findAll();
    }

    public void deactivateKey(Long id) {
        DigitalKey k = repository.findById(id).orElse(null);
        if (k != null) {
            k.setActive(false);
            repository.save(k);
        }
    }
}
