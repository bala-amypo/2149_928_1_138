package com.example.demo.controller;

import com.example.demo.model.KeyShareRequest;
import com.example.demo.service.KeyShareRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/key-share")
@Tag(name = "Key Share Requests")
public class KeyShareRequestController {

    private final KeyShareRequestService keyShareRequestService;

    public KeyShareRequestController(KeyShareRequestService keyShareRequestService) {
        this.keyShareRequestService = keyShareRequestService;
    }

    /**
     * Create a key share request
     */
    @PostMapping
    public KeyShareRequest create(@RequestBody KeyShareRequest request) {
        return keyShareRequestService.createShareRequest(request);
    }

    /**
     * Approve / Reject a request
     */
    @PutMapping("/{id}/status")
    public KeyShareRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return keyShareRequestService.updateStatus(id, status);
    }

    /**
     * Requests received by a guest
     */
    @GetMapping("/received/{guestId}")
    public List<KeyShareRequest> received(@PathVariable Long guestId) {
        return keyShareRequestService.receivedRequests(guestId);
    }

    /**
     * Requests sent by a guest
     */
    @GetMapping("/sent/{guestId}")
    public List<KeyShareRequest> sent(@PathVariable Long guestId) {
        return keyShareRequestService.sentRequests(guestId);
    }
}
