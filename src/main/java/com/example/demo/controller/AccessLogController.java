package com.example.demo.controller;

import com.example.demo.model.AccessLog;
import com.example.demo.service.AccessLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access-logs")
@Tag(name = "Access Logs")
public class AccessLogController {

    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @PostMapping
    public AccessLog createAccessLog(@RequestBody AccessLog accessLog) {
        return accessLogService.createLog(accessLog);
    }

    @GetMapping("/guest/{guestId}")
    public List<AccessLog> getLogsByGuest(@PathVariable Long guestId) {
        return accessLogService.getLogsByGuest(guestId);
    }

    @GetMapping("/key/{keyId}")
    public List<AccessLog> getLogsByKey(@PathVariable Long keyId) {
        return accessLogService.getLogsByKey(keyId);
    }
}
