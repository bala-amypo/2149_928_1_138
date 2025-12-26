package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {
    List<KeyShareRequest> findBySharedWithId(Long guestId);
    List<KeyShareRequest> findBySharedById(Long guestId);
}
