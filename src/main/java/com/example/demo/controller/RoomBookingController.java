package com.example.demo.controller;

import com.example.demo.entity.RoomBooking;
import com.example.demo.service.RoomBookingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class RoomBookingController {

    private final RoomBookingService service;

    public RoomBookingController(RoomBookingService service) {
        this.service = service;
    }

    @PostMapping
    public RoomBooking create(@RequestBody RoomBooking booking) {
        return service.createBooking(booking);
    }

    @GetMapping("/{id}")
    public RoomBooking get(@PathVariable Long id) {
        return service.getBooking(id);
    }

    @GetMapping
    public List<RoomBooking> getAll() {
        return service.getAllBookings();
    }
}
