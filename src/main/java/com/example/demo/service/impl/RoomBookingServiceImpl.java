package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.RoomBooking;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    // ✅ USED BY TESTS
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = null;
    }

    // ✅ USED BY SPRING
    public RoomBookingServiceImpl(RoomBookingRepository bookingRepository,
                                  GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {

        if (booking == null ||
            booking.getCheckInDate() == null ||
            booking.getCheckOutDate() == null ||
            !booking.getCheckInDate().isBefore(booking.getCheckOutDate())) {
            throw new IllegalArgumentException("Invalid booking dates");
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

        if (update.getRoomNumber() != null)
            existing.setRoomNumber(update.getRoomNumber());

        if (update.getCheckInDate() != null)
            existing.setCheckInDate(update.getCheckInDate());

        if (update.getCheckOutDate() != null)
            existing.setCheckOutDate(update.getCheckOutDate());

        if (update.getActive() != null)
            existing.setActive(update.getActive());

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
