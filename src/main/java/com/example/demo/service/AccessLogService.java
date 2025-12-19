package com.example.demo.service;

import com.example.demo.entity.AccessLog;
import java.util.List;

public interface AccessLogService {
    AccessLog create(AccessLog log);
    List<AccessLog> getAll();
}
