package com.example.demo.controller;

import com.example.demo.entity.KeyShareRequest;
import com.example.demo.service.KeyShareRequestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/key-share")
public class KeyShareRequestController {

    private final KeyShareRequestService service;

    public KeyShareRequestController(KeyShareRequestService service) {
        this.service = service;
    }

    @PostMapping
    public KeyShareRequest create(@RequestBody KeyShareRequest request) {
        return service.createRequest(request);
    }

    @GetMapping
    public List<KeyShareRequest> getAll() {
        return service.getAllRequests();
    }
}
