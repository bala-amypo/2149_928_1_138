package com.example.demo.controller;

import com.example.demo.entity.KeyShareRequest;
import com.example.demo.service.KeyShareRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/key-share")
public class KeyShareRequestController {

    private final KeyShareRequestService service;

    public KeyShareRequestController(KeyShareRequestService service) {
        this.service = service;
    }

    @PostMapping
    public KeyShareRequest create(@Valid @RequestBody KeyShareRequest request) {
        return service.save(request);
    }
}
