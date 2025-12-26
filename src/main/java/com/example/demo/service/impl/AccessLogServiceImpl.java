package com.example.demo.service.impl;

import com.example.demo.model.AccessLog;
import com.example.demo.repository.AccessLogRepository;
import com.example.demo.service.AccessLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repository;

    public AccessLogServiceImpl(AccessLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public AccessLog createLog(AccessLog log) {
        if (log.getAccessTime().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Access time cannot be in future");
        }
        return repository.save(log);
    }

    @Override
    public List<AccessLog> getLogsForKey(Long keyId) {
        return repository.findByDigitalKeyId(keyId);
    }

    @Override
    public List<AccessLog> getLogsForGuest(Long guestId) {
        return repository.findByGuestId(guestId);
    }
}
