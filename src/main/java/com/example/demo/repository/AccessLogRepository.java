package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByGuestId(Long guestId);
    List<AccessLog> findByDigitalKeyId(Long keyId);
}
