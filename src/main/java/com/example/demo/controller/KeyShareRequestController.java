package com.example.demo.controller;

import com.example.demo.entity.KeyShareRequest;
import com.example.demo.service.KeyShareRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/key-share-requests")
public class KeyShareRequestController {

    private final KeyShareRequestService service;

    public KeyShareRequestController(KeyShareRequestService service) {
        this.service = service;
    }

    @PostMapping
    public KeyShareRequest create(@RequestBody KeyShareRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<KeyShareRequest> getAll() {
        return service.getAll();
    }
}
