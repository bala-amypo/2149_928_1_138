package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);
    boolean existsByEmail(String email);
}

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> findByGuestId(Long guestId);
}

public interface DigitalKeyRepository extends JpaRepository<DigitalKey, Long> {
    Optional<DigitalKey> findByBookingIdAndActiveTrue(Long bookingId);
    List<DigitalKey> findByBookingGuestId(Long guestId);
}

public interface KeyShareRequestRepository extends JpaRepository<KeyShareRequest, Long> {
    List<KeyShareRequest> findBySharedWithId(Long guestId);
    List<KeyShareRequest> findBySharedById(Long guestId);
}

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByGuestId(Long guestId);
    List<AccessLog> findByDigitalKeyId(Long keyId);
}
