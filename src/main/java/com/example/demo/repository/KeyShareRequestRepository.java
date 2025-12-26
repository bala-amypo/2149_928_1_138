package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {
    List<KeyShareRequest> findBySharedById(Long guestId);
    List<KeyShareRequest> findBySharedWithId(Long guestId);
    boolean existsByDigitalKeyIdAndSharedWithIdAndStatus(
            Long digitalKeyId,
            Long sharedWithId,
            String status
    );
}
