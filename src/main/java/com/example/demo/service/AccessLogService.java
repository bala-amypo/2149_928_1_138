package com.example.demo.service;

import com.example.demo.entity.AccessLog;
import java.util.List;

public interface AccessLogService {
    AccessLog createLog(AccessLog log);
    List<AccessLog> getAllLogs();
}
