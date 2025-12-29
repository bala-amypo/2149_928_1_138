package com.example.demo.service.impl;

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

    // required for tests
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = null;
    }

    @Autowired
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository,
                                  GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("booking");
        }

        LocalDate in = booking.getCheckInDate();
        LocalDate out = booking.getCheckOutDate();

        if (in == null || out == null || !in.isBefore(out)) {
            throw new IllegalArgumentException("date");
        }

        booking.setActive(true);
        return bookingRepository.save(booking);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // 🔑 MUST RETURN NULL IF NOT FOUND
    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = bookingRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        LocalDate in = update.getCheckInDate() != null
                ? update.getCheckInDate()
                : existing.getCheckInDate();

        LocalDate out = update.getCheckOutDate() != null
                ? update.getCheckOutDate()
                : existing.getCheckOutDate();

        if (!in.isBefore(out)) {
            throw new IllegalArgumentException("date");
        }

        existing.setCheckInDate(in);
        existing.setCheckOutDate(out);

        if (update.getRoomNumber() != null) {
            existing.setRoomNumber(update.getRoomNumber());
        }

        return bookingRepository.save(existing);
    }

    @Override
    public void deactivateBooking(Long id) {
        RoomBooking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) return;

        booking.setActive(false);
        bookingRepository.save(booking);
    }

    @Override
    public List<RoomBooking> getBookingsForGuest(Long guestId) {
        if (guestId == null) return Collections.emptyList();
        return bookingRepository.findByGuestId(guestId);
    }
}
