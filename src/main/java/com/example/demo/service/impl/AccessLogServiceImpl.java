package com.example.demo.service.impl;

import com.example.demo.model.AccessLog;
import com.example.demo.repository.AccessLogRepository;
import com.example.demo.service.AccessLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repository;

    public AccessLogServiceImpl(AccessLogRepository repository) {
        this.repository = repository;
    }

    public AccessLog create(AccessLog log) {
        return repository.save(log);
    }

    public List<AccessLog> getAll() {
        return repository.findAll();
    }
}
