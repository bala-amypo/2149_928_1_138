package com.example.demo.repository;

import com.example.demo.model.KeyShareRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {

    // Requests RECEIVED by a guest
    List<KeyShareRequest> findBySharedWithId(Long guestId);

    // Requests SENT by a guest
    List<KeyShareRequest> findBySharedById(Long guestId);
}
