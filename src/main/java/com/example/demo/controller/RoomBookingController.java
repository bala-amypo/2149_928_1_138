package com.example.demo.controller;

import com.example.demo.entity.RoomBooking;
import com.example.demo.repository.RoomBookingRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class RoomBookingController {

    private final RoomBookingRepository repository;

    public RoomBookingController(RoomBookingRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public RoomBooking create(@RequestBody RoomBooking booking) {
        return repository.save(booking);
    }
}
