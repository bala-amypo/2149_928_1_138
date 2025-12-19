package com.example.demo.service.impl;

import com.example.demo.entity.RoomBooking;
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

    public RoomBooking createBooking(RoomBooking booking) {
        return repository.save(booking);
    }

    public RoomBooking updateBooking(Long id, RoomBooking booking) {
        booking.setId(id);
        return repository.save(booking);
    }

    public RoomBooking getBooking(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<RoomBooking> getAllBookings() {
        return repository.findAll();
    }

    public void deactivateBooking(Long id) {
        RoomBooking b = repository.findById(id).orElse(null);
        if (b != null) {
            b.setActive(false);
            repository.save(b);
        }
    }
}
