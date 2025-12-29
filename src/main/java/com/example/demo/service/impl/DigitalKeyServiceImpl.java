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

        if (bookingId == null) throw new IllegalArgumentException("booking");

        RoomBooking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) throw new IllegalArgumentException("booking");

        if (!Boolean.TRUE.equals(booking.getActive()))
            throw new IllegalStateException("inactive");

        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(Instant.now());
        key.setExpiresAt(Instant.now().plusSeconds(3600));
        key.setActive(true);

        return keyRepository.save(key);
    }

    @Override
    public DigitalKey getKeyById(Long id) {
        if (id == null) return null;
        return keyRepository.findById(id).orElse(null);
    }

    @Override
    public DigitalKey getActiveKeyForBooking(Long bookingId) {
        if (bookingId == null) return null;
        return keyRepository.findByBookingIdAndActiveTrue(bookingId).orElse(null);
    }

    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        if (guestId == null) return Collections.emptyList();
        return keyRepository.findByBookingGuestId(guestId);
    }
}
