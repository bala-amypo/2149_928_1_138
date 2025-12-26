package com.example.demo.controller;

import com.example.demo.model.RoomBooking;
import com.example.demo.service.RoomBookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class RoomBookingController {

    private final RoomBookingService roomBookingService;

    public RoomBookingController(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }

    // ✅ Create booking
    @PostMapping(consumes = "application/json", produces = "application/json")
    public RoomBooking create(@RequestBody RoomBooking booking) {
        return roomBookingService.createBooking(booking);
    }

    // ✅ Get booking by ID
    @GetMapping("/{id}")
    public RoomBooking getById(@PathVariable Long id) {
        return roomBookingService.getBookingById(id);
    }

    // ✅ Get bookings for guest
    @GetMapping("/guest/{guestId}")
    public List<RoomBooking> getForGuest(@PathVariable Long guestId) {
        return roomBookingService.getBookingsForGuest(guestId);
    }

    // ✅ Update booking
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public RoomBooking update(
            @PathVariable Long id,
            @RequestBody RoomBooking booking) {
        return roomBookingService.updateBooking(id, booking);
    }

    // ✅ Deactivate booking
    @PutMapping("/{id}/deactivate")
    public RoomBooking deactivate(@PathVariable Long id) {
        roomBookingService.deactivateBooking(id);
        return roomBookingService.getBookingById(id);
    }
}
