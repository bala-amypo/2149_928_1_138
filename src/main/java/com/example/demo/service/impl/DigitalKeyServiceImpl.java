package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DigitalKey;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.service.DigitalKeyService;

@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository digitalKeyRepository;

    public DigitalKeyServiceImpl(DigitalKeyRepository digitalKeyRepository) {
        this.digitalKeyRepository = digitalKeyRepository;
    }

    @Override
    public DigitalKey createDigitalKey(DigitalKey digitalKey) {
        return digitalKeyRepository.save(digitalKey);
    }

    @Override
    public DigitalKey getDigitalKeyById(Long id) {
        return digitalKeyRepository.findById(id).orElse(null);
    }

    @Override
    public List<DigitalKey> getAllDigitalKeys() {
        return digitalKeyRepository.findAll();
    }

    @Override
    public DigitalKey updateDigitalKey(Long id, DigitalKey digitalKey) {
        digitalKey.setId(id);
        return digitalKeyRepository.save(digitalKey);
    }

    @Override
    public void deleteDigitalKey(Long id) {
        digitalKeyRepository.deleteById(id);
    }
}
