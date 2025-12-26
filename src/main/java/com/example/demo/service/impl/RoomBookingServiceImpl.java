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

    public RoomBookingServiceImpl(
            RoomBookingRepository bookingRepository,
            GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("booking required");
        }

        if (booking.getGuest() == null || booking.getGuest().getId() == null) {
            throw new IllegalArgumentException("guest required");
        }

        Guest guest = guestRepository.findById(booking.getGuest().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        booking.setGuest(guest);

        if (booking.getActive() == null) {
            booking.setActive(true);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<RoomBooking> getBookingsForGuest(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (update.getRoomNumber() != null)
            existing.setRoomNumber(update.getRoomNumber());

        if (update.getCheckInDate() != null)
            existing.setCheckInDate(update.getCheckInDate());

        if (update.getCheckOutDate() != null)
            existing.setCheckOutDate(update.getCheckOutDate());

        if (update.getActive() != null)
            existing.setActive(update.getActive());

        if (update.getRoommates() != null)
            existing.setRoommates(update.getRoommates());

        return bookingRepository.save(existing);
    }

    @Override
    public void deactivateBooking(Long id) {

        RoomBooking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        booking.setActive(false);
        bookingRepository.save(booking);
    }
}
