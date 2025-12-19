package com.example.demo.controller;

import com.example.demo.entity.AccessLog;
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

    @GetMapping
    public List<AccessLog> getAll() {
        return service.getAllLogs();
    }
}
