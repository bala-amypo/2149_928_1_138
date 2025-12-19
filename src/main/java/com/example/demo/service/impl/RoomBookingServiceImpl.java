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

    public RoomBooking create(RoomBooking booking) {
        return repository.save(booking);
    }

    public List<RoomBooking> getAll() {
        return repository.findAll();
    }
}
