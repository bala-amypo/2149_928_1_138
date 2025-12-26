package com.example.demo.controller;

import com.example.demo.model.DigitalKey;
import com.example.demo.service.DigitalKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/digital-keys")
public class DigitalKeyController {

    private final DigitalKeyService service;

    public DigitalKeyController(DigitalKeyService service) {
        this.service = service;
    }

    // ✅ Generate key for a booking
    @PostMapping("/generate/{bookingId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DigitalKey generate(@PathVariable Long bookingId) {
        return service.generateKey(bookingId);
    }

    // ✅ Get key by ID
    @GetMapping("/{id}")
    public DigitalKey get(@PathVariable Long id) {
        return service.getKeyById(id);
    }

    // ✅ Get active key for booking
    @GetMapping("/booking/{bookingId}")
    public DigitalKey getActive(@PathVariable Long bookingId) {
        return service.getActiveKeyForBooking(bookingId);
    }

    // ✅ Get all keys for guest
    @GetMapping("/guest/{guestId}")
    public List<DigitalKey> getByGuest(@PathVariable Long guestId) {
        return service.getKeysForGuest(guestId);
    }
}
