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

    @PostMapping
    public AccessLog create(@RequestBody AccessLog log) {
        return service.createLog(log);
    }

    @GetMapping("/key/{keyId}")
    public List<AccessLog> getForKey(@PathVariable Long keyId) {
        return service.getLogsForKey(keyId);
    }

    @GetMapping("/guest/{guestId}")
    public List<AccessLog> getForGuest(@PathVariable Long guestId) {
        return service.getLogsForGuest(guestId);
    }
}
