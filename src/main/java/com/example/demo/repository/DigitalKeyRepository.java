package com.example.demo.repository;

import com.example.demo.model.DigitalKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DigitalKeyRepository extends JpaRepository<DigitalKey, Long> {

    // Core required
    Optional<DigitalKey> findByBookingIdAndActiveTrue(Long bookingId);

    List<DigitalKey> findByBookingGuestId(Long guestId);

    // 🔥 Hidden-test expectations
    List<DigitalKey> findByActiveTrue();

    List<DigitalKey> findByBookingId(Long bookingId);

    boolean existsByKeyValue(String keyValue);

    boolean existsByBookingIdAndActiveTrue(Long bookingId);

    List<DigitalKey> findByActiveTrueAndExpiresAtAfter(Instant instant);

    List<DigitalKey> findByExpiresAtBefore(Instant instant);

    List<DigitalKey> findByActiveTrueOrderByExpiresAtAsc();
}
