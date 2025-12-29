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

    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null) throw new IllegalArgumentException("booking");

        if (booking.getCheckInDate() == null ||
            booking.getCheckOutDate() == null ||
            !booking.getCheckInDate().isBefore(booking.getCheckOutDate())) {
            throw new IllegalArgumentException("date");
        }

        booking.setActive(true);
        return bookingRepository.save(booking);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        if (id == null) return null;
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {
        RoomBooking b = bookingRepository.findById(id).orElse(null);
        if (b == null) return null;

        return bookingRepository.save(b);
    }

    @Override
    public void deactivateBooking(Long id) {
        RoomBooking b = bookingRepository.findById(id).orElse(null);
        if (b == null) return;
        b.setActive(false);
        bookingRepository.save(b);
    }

    @Override
    public List<RoomBooking> getBookingsForGuest(Long guestId) {
        if (guestId == null) return Collections.emptyList();
        return bookingRepository.findByGuestId(guestId);
    }
}
