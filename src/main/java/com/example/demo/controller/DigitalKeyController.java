package com.example.demo.controller;

import com.example.demo.model.DigitalKey;
import com.example.demo.service.DigitalKeyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/digital-keys")
@Tag(name = "Digital Keys")
public class DigitalKeyController {

    private final DigitalKeyService digitalKeyService;

    public DigitalKeyController(DigitalKeyService digitalKeyService) {
        this.digitalKeyService = digitalKeyService;
    }

    /**
     * Generate a digital key for a booking
     */
    @PostMapping("/booking/{bookingId}")
    public DigitalKey generateKey(@PathVariable Long bookingId) {
        return digitalKeyService.generateKey(bookingId);
    }

    /**
     * Get all digital keys for a guest
     */
    @GetMapping("/guest/{guestId}")
    public List<DigitalKey> getKeysForGuest(@PathVariable Long guestId) {
        return digitalKeyService.getKeysForGuest(guestId);
    }
}
