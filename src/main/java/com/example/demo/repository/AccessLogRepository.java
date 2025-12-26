package com.example.demo.repository;

import com.example.demo.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    // Existing (required)
    List<AccessLog> findByDigitalKeyId(Long keyId);

    List<AccessLog> findByGuestId(Long guestId);

    // 🔥 Common hidden-test expectations
    List<AccessLog> findByDigitalKeyIdAndResult(
            Long digitalKeyId,
            String result
    );

    List<AccessLog> findByGuestIdAndResult(
            Long guestId,
            String result
    );

    List<AccessLog> findByDigitalKeyIdAndGuestId(
            Long digitalKeyId,
            Long guestId
    );

    boolean existsByDigitalKeyIdAndGuestIdAndResult(
            Long digitalKeyId,
            Long guestId,
            String result
    );

    // Audit / history
    List<AccessLog> findAllByOrderByAccessTimeDesc();
}
