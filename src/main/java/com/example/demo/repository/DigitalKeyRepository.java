package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
public interface DigitalKeyRepository extends JpaRepository<DigitalKey, Long> {
    Optional<DigitalKey> findByBookingIdAndActiveTrue(Long bookingId);
    List<DigitalKey> findByBookingGuestId(Long guestId);
}
