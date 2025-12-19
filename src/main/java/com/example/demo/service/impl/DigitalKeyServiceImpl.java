package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DigitalKey;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.service.DigitalKeyService;

@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository repository;

    public DigitalKeyServiceImpl(DigitalKeyRepository repository) {
        this.repository = repository;
    }

    @Override
    public DigitalKey create(DigitalKey digitalKey) {
        return repository.save(digitalKey);
    }

    @Override
    public DigitalKey getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<DigitalKey> getAll() {
        return repository.findAll();
    }

    @Override
    public DigitalKey update(Long id, DigitalKey digitalKey) {
        digitalKey.setId(id);
        return repository.save(digitalKey);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
