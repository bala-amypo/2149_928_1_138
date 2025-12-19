package com.example.demo.repository;

import com.example.demo.entity.DigitalKey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DigitalKeyRepository {

    private final List<DigitalKey> keys = new ArrayList<>();

    public DigitalKey save(DigitalKey key) {
        keys.add(key);
        return key;
    }

    public List<DigitalKey> findAll() {
        return keys;
    }
}
