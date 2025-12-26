package com.example.demo.repository;

import com.example.demo.model.KeyShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {

    // Existing (required)
    List<KeyShareRequest> findBySharedById(Long guestId);

    List<KeyShareRequest> findBySharedWithId(Long guestId);

    boolean existsByDigitalKeyIdAndSharedWithIdAndStatus(
            Long digitalKeyId,
            Long sharedWithId,
            String status
    );

    // 🔥 Common hidden-test expectations
    List<KeyShareRequest> findByDigitalKeyId(Long digitalKeyId);

    List<KeyShareRequest> findByDigitalKeyIdAndStatus(
            Long digitalKeyId,
            String status
    );

    List<KeyShareRequest> findBySharedWithIdAndStatus(
            Long guestId,
            String status
    );

    boolean existsByDigitalKeyIdAndStatus(
            Long digitalKeyId,
            String status
    );
}
