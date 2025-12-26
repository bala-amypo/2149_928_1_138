package com.example.demo.controller;

import com.example.demo.model.DigitalKey;
import com.example.demo.service.DigitalKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/digital-keys")
public class DigitalKeyController {

    private final DigitalKeyService service;

    public DigitalKeyController(DigitalKeyService service) {
        this.service = service;
    }

    @PostMapping("/generate/{bookingId}")
    public DigitalKey generate(@PathVariable Long bookingId) {
        return service.generateKey(bookingId);
    }

    @GetMapping("/{id}")
    public DigitalKey get(@PathVariable Long id) {
        return service.getKeyById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public DigitalKey getActive(@PathVariable Long bookingId) {
        return service.getActiveKeyForBooking(bookingId);
    }

    @GetMapping("/guest/{guestId}")
    public List<DigitalKey> getByGuest(@PathVariable Long guestId) {
        return service.getKeysForGuest(guestId);
    }
}
