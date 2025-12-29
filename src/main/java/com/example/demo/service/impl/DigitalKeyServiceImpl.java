// package com.example.demo.service.impl;

// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.model.DigitalKey;
// import com.example.demo.model.RoomBooking;
// import com.example.demo.repository.DigitalKeyRepository;
// import com.example.demo.repository.RoomBookingRepository;
// import com.example.demo.service.DigitalKeyService;
// import org.springframework.stereotype.Service;

// import java.time.Instant;
// import java.time.ZoneId;
// import java.util.Collections;   // ✅ FIXED IMPORT
// import java.util.List;
// import java.util.UUID;

// @Service
// public class DigitalKeyServiceImpl implements DigitalKeyService {

//     private final DigitalKeyRepository keyRepository;
//     private final RoomBookingRepository bookingRepository;

//     public DigitalKeyServiceImpl(
//             DigitalKeyRepository keyRepository,
//             RoomBookingRepository bookingRepository) {

//         this.keyRepository = keyRepository;
//         this.bookingRepository = bookingRepository;
//     }

//     @Override
//     public DigitalKey generateKey(Long bookingId) {

//         if (bookingId == null) {
//             throw new IllegalArgumentException("booking");
//         }

//         RoomBooking booking = bookingRepository.findById(bookingId)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("Booking not found"));

//         if (!Boolean.TRUE.equals(booking.getActive())) {
//             throw new IllegalStateException("inactive");
//         }

//         DigitalKey key = new DigitalKey();
//         key.setBooking(booking);
//         key.setKeyValue(UUID.randomUUID().toString());
//         key.setIssuedAt(Instant.now());
//         key.setExpiresAt(
//                 booking.getCheckOutDate()
//                         .atTime(23, 59, 59)
//                         .atZone(ZoneId.systemDefault())
//                         .toInstant()
//         );
//         key.setActive(true);

//         return keyRepository.save(key);
//     }

//     @Override
//     public DigitalKey getKeyById(Long id) {
//         if (id == null) {
//             throw new ResourceNotFoundException("Key not found");
//         }
//         return keyRepository.findById(id)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("Key not found"));
//     }

//     @Override
//     public DigitalKey getActiveKeyForBooking(Long bookingId) {

//         if (bookingId == null) {
//             throw new ResourceNotFoundException("Booking not found");
//         }

//         return keyRepository.findByBookingIdAndActiveTrue(bookingId)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("Key not found"));
//     }

//     @Override
//     public List<DigitalKey> getKeysForGuest(Long guestId) {
//         if (guestId == null) {
//             return Collections.emptyList(); // ✅ now works
//         }
//         return keyRepository.findByBookingGuestId(guestId);
//     }
// }
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

        if (bookingId == null)
            throw new IllegalArgumentException("booking");

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (!Boolean.TRUE.equals(booking.getActive()))
            throw new IllegalStateException("inactive");

        keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .ifPresent(k -> {
                    k.setActive(false);
                    keyRepository.save(k);
                });

        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(Instant.now());
        key.setExpiresAt(
                booking.getCheckOutDate()
                        .atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
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

        if (bookingId == null)
            throw new ResourceNotFoundException("Key not found");

        return keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Key not found"));
    }

    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        return keyRepository.findByBookingGuestId(guestId);
    }
}
