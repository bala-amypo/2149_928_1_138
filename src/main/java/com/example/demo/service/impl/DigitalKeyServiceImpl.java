package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.DigitalKeyService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository keyRepository;
    private final RoomBookingRepository bookingRepository;

    public DigitalKeyServiceImpl(DigitalKeyRepository keyRepository,
                                 RoomBookingRepository bookingRepository) {
        this.keyRepository = keyRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public DigitalKey generateKey(Long bookingId) {

        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID missing");
        }

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        // ✅ REQUIRED: IllegalStateException WITH MESSAGE
        if (!Boolean.TRUE.equals(booking.getActive())) {
            throw new IllegalStateException("Booking is inactive");
        }

        // deactivate existing active key
        keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .ifPresent(k -> {
                    k.setActive(false);
                    keyRepository.save(k);
                });

        Instant issuedAt = Instant.now();
        Instant expiresAt =
                booking.getCheckOutDate()
                        .atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();

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
                        new ResourceNotFoundException("Key not found"));
    }

    @Override
public DigitalKey getActiveKeyForBooking(Long bookingId) {

    if (bookingId == null) {
        return null;
    }

    DigitalKey key = keyRepository
            .findByBookingIdAndActiveTrue(bookingId)
            .orElse(null);

    if (key == null) {
        return null;
    }

    if (key.getExpiresAt() != null &&
        key.getExpiresAt().isBefore(Instant.now())) {
        return null;
    }

    return key;
}


    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        return keyRepository.findByBookingGuestId(guestId);
    }
}
