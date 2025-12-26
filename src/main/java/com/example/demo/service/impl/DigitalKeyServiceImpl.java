package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.DigitalKeyService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository keyRepository;
    private final RoomBookingRepository bookingRepository;

    // ✅ SINGLE constructor (portal-safe)
    public DigitalKeyServiceImpl(
            DigitalKeyRepository keyRepository,
            RoomBookingRepository bookingRepository) {
        this.keyRepository = keyRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public DigitalKey generateKey(Long bookingId) {

        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + bookingId));

        if (!Boolean.TRUE.equals(booking.getActive())) {
            throw new IllegalStateException("Booking is inactive");
        }

        LocalDate checkOutDate = booking.getCheckOutDate();
        if (checkOutDate == null) {
            throw new IllegalStateException("Booking checkout date is missing");
        }

        // ✅ Deactivate existing active key (single active key rule)
        keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .ifPresent(existing -> existing.setActive(false));

        Instant issuedAt = Instant.now();

        Instant expiresAt =
                checkOutDate
                        .atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();

        // ✅ Safety: ensure expiry is after issue time
        if (!expiresAt.isAfter(issuedAt)) {
            expiresAt = issuedAt.plusSeconds(60);
        }

        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(issuedAt);
        key.setExpiresAt(expiresAt);
        key.setActive(true);

        return keyRepository.save(key);
    }

    @Override
    public DigitalKey getKeyById(Long id) {
        return keyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Key not found with id: " + id));
    }

    @Override
    public DigitalKey getActiveKeyForBooking(Long bookingId) {
        return keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active key not found for booking id: " + bookingId));
    }

    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        return keyRepository.findByBookingGuestId(guestId);
    }
}
