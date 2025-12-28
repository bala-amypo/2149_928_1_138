package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    // ✅ REQUIRED BY TESTS (Mockito uses this)
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = null;
    }

    // ✅ REQUIRED BY SPRING (explicitly chosen)
    @Autowired
    public RoomBookingServiceImpl(
            RoomBookingRepository bookingRepository,
            GuestRepository guestRepository) {

        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

   @Override
public RoomBooking createBooking(RoomBooking booking) {

    if (booking == null) {
        throw new IllegalArgumentException("booking missing");
    }

    if (booking.getGuest() == null || booking.getGuest().getId() == null) {
        throw new IllegalArgumentException("guest missing");
    }

    LocalDate in = booking.getCheckInDate();
    LocalDate out = booking.getCheckOutDate();

    if (in == null || out == null || !in.isBefore(out)) {
        throw new IllegalArgumentException("invalid dates");
    }

    if (booking.getRoomNumber() == null ||
        booking.getRoomNumber().trim().isEmpty()) {
        throw new IllegalArgumentException("room missing");
    }

    booking.setActive(true);
    return bookingRepository.save(booking);
}


    @Override
    public RoomBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        LocalDate in = update.getCheckInDate() != null
                ? update.getCheckInDate()
                : existing.getCheckInDate();

        LocalDate out = update.getCheckOutDate() != null
                ? update.getCheckOutDate()
                : existing.getCheckOutDate();

        if (!in.isBefore(out)) {
            throw new IllegalArgumentException("invalid booking dates");
        }

        if (update.getRoomNumber() != null &&
            !update.getRoomNumber().trim().isEmpty()) {
            existing.setRoomNumber(update.getRoomNumber());
        }

        existing.setCheckInDate(in);
        existing.setCheckOutDate(out);

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

        return bookingRepository.findByGuestId(guestId);
    }
}
