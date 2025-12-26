package com.example.demo.controller;

import com.example.demo.model.RoomBooking;
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
        return service.getBookingById(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<RoomBooking> getByGuest(@PathVariable Long guestId) {
        return service.getBookingsForGuest(guestId);
    }

    @PutMapping("/{id}")
    public RoomBooking update(@PathVariable Long id, @RequestBody RoomBooking booking) {
        return service.updateBooking(id, booking);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateBooking(id);
    }
}
