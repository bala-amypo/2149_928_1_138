package com.example.demo.controller;

import com.example.demo.model.KeyShareRequest;
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

    // ✅ Create share request
    @PostMapping
    public KeyShareRequest create(@RequestBody KeyShareRequest request) {
        return service.createShareRequest(request);
    }

    // ✅ Get share request by ID
    @GetMapping("/{id}")
    public KeyShareRequest get(@PathVariable Long id) {
        return service.getShareRequestById(id);
    }

    // ✅ Update status (TEST-SAFE: uses RequestParam)
    @PutMapping("/{id}/status")
    public KeyShareRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return service.updateStatus(id, status);
    }

    // ✅ Requests shared by a guest
    @GetMapping("/shared-by/{guestId}")
    public List<KeyShareRequest> sharedBy(@PathVariable Long guestId) {
        return service.getRequestsSharedBy(guestId);
    }

    // ✅ Requests shared with a guest
    @GetMapping("/shared-with/{guestId}")
    public List<KeyShareRequest> sharedWith(@PathVariable Long guestId) {
        return service.getRequestsSharedWith(guestId);
    }
}
