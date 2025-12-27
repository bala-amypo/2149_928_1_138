package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Guest;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    // ✅ SINGLE constructor (Spring + tests compatible)
    public RoomBookingServiceImpl(
            RoomBookingRepository bookingRepository,
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
            throw new IllegalArgumentException("Guest required");
        }

        Guest guest = guestRepository.findById(booking.getGuest().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        booking.setGuest(guest);
        booking.setActive(true);

        return bookingRepository.save(booking);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = getBookingById(id);

        if (update.getRoomNumber() != null) {
            existing.setRoomNumber(update.getRoomNumber());
        }
        if (update.getCheckInDate() != null) {
            existing.setCheckInDate(update.getCheckInDate());
        }
        if (update.getCheckOutDate() != null) {
            existing.setCheckOutDate(update.getCheckOutDate());
        }
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
        return bookingRepository.findByGuestId(guestId);
    }
}
