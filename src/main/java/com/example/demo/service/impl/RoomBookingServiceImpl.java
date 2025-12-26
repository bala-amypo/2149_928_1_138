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

    private final RoomBookingRepository roomBookingRepository;
    private final GuestRepository guestRepository;

    // ✅ PRIMARY CONSTRUCTOR (Spring)
    public RoomBookingServiceImpl(RoomBookingRepository roomBookingRepository,
                                  GuestRepository guestRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.guestRepository = guestRepository;
    }

    // ✅ SECONDARY CONSTRUCTOR (JUnit tests)
    public RoomBookingServiceImpl(RoomBookingRepository roomBookingRepository) {
        this.roomBookingRepository = roomBookingRepository;
        this.guestRepository = null; // safe for tests
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (!booking.getCheckInDate().isBefore(booking.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in");
        }

        // Tests may not inject GuestRepository
        if (guestRepository != null && booking.getGuest() != null) {
            Guest guest = guestRepository.findById(booking.getGuest().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Guest not found"));
            booking.setGuest(guest);
        }

        booking.setActive(true);
        return roomBookingRepository.save(booking);
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking update) {

        RoomBooking existing = roomBookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found " + id));

        if (!update.getCheckInDate().isBefore(update.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in");
        }

        existing.setRoomNumber(update.getRoomNumber());
        existing.setCheckInDate(update.getCheckInDate());
        existing.setCheckOutDate(update.getCheckOutDate());
        existing.setActive(update.getActive());

        return roomBookingRepository.save(existing);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return roomBookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found " + id));
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
