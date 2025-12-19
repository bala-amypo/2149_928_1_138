package com.example.demo.service;

import com.example.demo.model.AccessLog;
import java.util.List;

public interface AccessLogService {

    AccessLog createLog(AccessLog accessLog);

    List<AccessLog> getLogsByGuest(Long guestId);

    List<AccessLog> getLogsByKey(Long keyId);
}
