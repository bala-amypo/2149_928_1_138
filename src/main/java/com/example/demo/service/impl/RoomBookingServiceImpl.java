// src/main/java/com/example/demo/service/impl/RoomBookingServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.RoomBooking;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoomBookingRepository;
import com.example.demo.service.RoomBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository repository;

    public RoomBookingServiceImpl(RoomBookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoomBooking createBooking(RoomBooking booking) {
        return repository.save(booking);
    }

    @Override
    public RoomBooking updateBooking(Long id, RoomBooking booking) {
        RoomBooking existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        existing.setRoomNumber(booking.getRoomNumber());
        existing.setGuestName(booking.getGuestName());
        return repository.save(existing);
    }

    @Override
    public RoomBooking getBookingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<RoomBooking> getAllBookings() {
        return repository.findAll();
    }
}
