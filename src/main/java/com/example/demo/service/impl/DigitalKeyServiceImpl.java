package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.DigitalKeyService;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
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
        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found " + bookingId));

        if (!booking.getActive()) {
            throw new IllegalStateException("Booking is inactive");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(now);
        key.setExpiresAt(new Timestamp(now.getTime() + 86400000)); // +24h
        key.setActive(true);

        return keyRepository.save(key);
    }

    @Override
    public DigitalKey getKeyById(Long id) {
        return keyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Key not found " + id));
    }

    @Override
    public DigitalKey getActiveKeyForBooking(Long bookingId) {
        return keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Active key not found " + bookingId));
    }

    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        return keyRepository.findByBookingGuestId(guestId);
    }
}
