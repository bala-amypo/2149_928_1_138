package com.example.demo.repository;

import com.example.demo.model.AccessLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccessLogRepository {

    private final List<AccessLog> logs = new ArrayList<>();

    public AccessLog save(AccessLog log) {
        logs.add(log);
        return log;
    }

    public List<AccessLog> findAll() {
        return logs;
    }
}
