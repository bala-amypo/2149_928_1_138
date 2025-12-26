package com.example.demo.repository;

import com.example.demo.model.KeyShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {

    List<KeyShareRequest> findBySharedById(Long guestId);

    List<KeyShareRequest> findBySharedWithId(Long guestId);

    // ✅ Required for access validation (tests + runtime)
    boolean existsByDigitalKeyIdAndSharedWithIdAndStatus(
            Long digitalKeyId,
            Long sharedWithId,
            String status
    );
}
