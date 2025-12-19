package com.example.demo.service.impl;

import com.example.demo.model.AccessLog;
import com.example.demo.repository.AccessLogRepository;
import com.example.demo.service.AccessLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public AccessLogServiceImpl(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    @Override
    public AccessLog createLog(AccessLog accessLog) {

        // ❗ Test-required validation
        if (accessLog.getAccessTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("future");
        }

        accessLog.setResult("SUCCESS");
        return accessLogRepository.save(accessLog);
    }

    @Override
    public List<AccessLog> getLogsByGuest(Long guestId) {
        return accessLogRepository.findByGuestId(guestId);
    }

    @Override
    public List<AccessLog> getLogsByKey(Long keyId) {
        return accessLogRepository.findByDigitalKeyId(keyId);
    }
}
