package com.example.demo.controller;

import com.example.demo.model.AccessLog;
import com.example.demo.service.AccessLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access-logs")
public class AccessLogController {

    private final AccessLogService service;

    public AccessLogController(AccessLogService service) {
        this.service = service;
    }

    @PostMapping
    public AccessLog create(@RequestBody AccessLog log) {
        return service.create(log);
    }

    @GetMapping
    public List<AccessLog> getAll() {
        return service.getAll();
    }
}
