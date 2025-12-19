// src/main/java/com/example/demo/controller/RoomBookingController.java
package com.example.demo.controller;

import com.example.demo.entity.RoomBooking;
import com.example.demo.service.RoomBookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-bookings")
public class RoomBookingController {

    private final RoomBookingService service;

    public RoomBookingController(RoomBookingService service) {
        this.service = service;
    }

    @PostMapping
    public RoomBooking create(@RequestBody RoomBooking booking) {
        return service.createBooking(booking);
    }

    @PutMapping("/{id}")
    public RoomBooking update(@PathVariable Long id, @RequestBody RoomBooking booking) {
        return service.updateBooking(id, booking);
    }

    @GetMapping("/{id}")
    public RoomBooking get(@PathVariable Long id) {
        return service.getBookingById(id);
    }

    @GetMapping
    public List<RoomBooking> getAll() {
        return service.getAllBookings();
    }
}
