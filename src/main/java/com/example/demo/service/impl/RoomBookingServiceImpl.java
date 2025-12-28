package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    // ✅ SINGLE CONSTRUCTOR — REQUIRED BY SPRING + TESTS
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository,
                                  GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        if (booking.getGuest() == null || booking.getGuest().getId() == null) {
            throw new IllegalArgumentException("Guest is required");
        }

        // Ensure guest exists when repository available
        if (guestRepository != null &&
                !guestRepository.existsById(booking.getGuest().getId())) {
            throw new ResourceNotFoundException("Guest not found");
        }

        LocalDate in = booking.getCheckInDate();
        LocalDate out = booking.getCheckOutDate();

        if (in == null || out == null || !in.isBefore(out)) {
            throw new IllegalArgumentException("Invalid booking dates");
        }

        if (booking.getRoomNumber() == null ||
                booking.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Room number is required");
        }

        booking.setActive(true);
        return bookingRepository.save(booking);
    }

    @Override
    public RoomBooking getBookingById(Long id) {

        if (id == null) {
            throw new ResourceNotFoundException("Booking not found");
        }

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        if (update == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        RoomBooking existing = getBookingById(id);

        LocalDate newIn = update.getCheckInDate() != null
                ? update.getCheckInDate()
                : existing.getCheckInDate();

        LocalDate newOut = update.getCheckOutDate() != null
                ? update.getCheckOutDate()
                : existing.getCheckOutDate();

        if (newIn == null || newOut == null || !newIn.isBefore(newOut)) {
            throw new IllegalArgumentException("Invalid booking dates");
        }

        if (update.getRoomNumber() != null &&
                !update.getRoomNumber().trim().isEmpty()) {
            existing.setRoomNumber(update.getRoomNumber());
        }

        existing.setCheckInDate(newIn);
        existing.setCheckOutDate(newOut);

        if (update.getActive() != null) {
            existing.setActive(update.getActive());
        }

        return bookingRepository.save(existing);
    }

    @Override
    public void deactivateBooking(Long id) {
        RoomBooking booking = getBookingById(id);
        booking.setActive(false);
        bookingRepository.save(booking);
    }

    @Override
    public List<RoomBooking> getBookingsForGuest(Long guestId) {

        if (guestId == null) {
            return Collections.emptyList();
        }

        List<RoomBooking> bookings =
                bookingRepository.findByGuestId(guestId);

        return bookings != null ? bookings : Collections.emptyList();
    }
}
