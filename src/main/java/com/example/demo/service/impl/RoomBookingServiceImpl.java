package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Guest;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final GuestRepository guestRepository;

    // ✅ SINGLE constructor (Spring + portal)
    public RoomBookingServiceImpl(RoomBookingRepository roomBookingRepository,
                                  GuestRepository guestRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        LocalDate checkIn = booking.getCheckInDate();
        LocalDate checkOut = booking.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        // ✅ Validate guest only if provided
        if (booking.getGuest() != null && booking.getGuest().getId() != null) {
            Guest guest = guestRepository.findById(booking.getGuest().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Guest not found with id: "
                                    + booking.getGuest().getId()));
            booking.setGuest(guest);
        }

        // ✅ Default active flag
        booking.setActive(true);

        return roomBookingRepository.save(booking);
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = roomBookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));

        LocalDate checkIn = update.getCheckInDate();
        LocalDate checkOut = update.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        // ✅ Selective update
        if (update.getRoomNumber() != null) {
            existing.setRoomNumber(update.getRoomNumber());
        }

        existing.setCheckInDate(checkIn);
        existing.setCheckOutDate(checkOut);

        if (update.getActive() != null) {
            existing.setActive(update.getActive());
        }

        return roomBookingRepository.save(existing);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return roomBookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Override
    public List<RoomBooking> getBookingsForGuest(Long guestId) {
        return roomBookingRepository.findByGuestId(guestId);
    }

    @Override
    public void deactivateBooking(Long id) {
        RoomBooking booking = getBookingById(id);
        booking.setActive(false);
        roomBookingRepository.save(booking);
    }
}
