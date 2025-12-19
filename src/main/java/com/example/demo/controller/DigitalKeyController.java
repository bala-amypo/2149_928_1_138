package com.example.demo.controller;

import com.example.demo.entity.DigitalKey;
import com.example.demo.service.DigitalKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/digital-keys")
public class DigitalKeyController {

    private final DigitalKeyService service;

    public DigitalKeyController(DigitalKeyService service) {
        this.service = service;
    }

    @PostMapping
    public DigitalKey create(@RequestBody DigitalKey key) {
        return service.create(key);
    }

    @GetMapping
    public List<DigitalKey> getAll() {
        return service.getAll();
    }
}
