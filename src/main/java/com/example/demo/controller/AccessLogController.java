package com.example.demo.controller;

import com.example.demo.model.AccessLog;
import com.example.demo.service.AccessLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access-logs")
public class AccessLogController {

    private final AccessLogService service;

    public AccessLogController(AccessLogService service) {
        this.service = service;
    }

    // ✅ Create access log (TEST-SAFE)
    @PostMapping
    public AccessLog create(@RequestBody AccessLog log) {
        return service.createLog(log);
    }

    // ✅ Get logs by digital key
    @GetMapping("/key/{keyId}")
    public List<AccessLog> byKey(@PathVariable Long keyId) {
        return service.getLogsForKey(keyId);
    }

    // ✅ Get logs by guest
    @GetMapping("/guest/{guestId}")
    public List<AccessLog> byGuest(@PathVariable Long guestId) {
        return service.getLogsForGuest(guestId);
    }
}
