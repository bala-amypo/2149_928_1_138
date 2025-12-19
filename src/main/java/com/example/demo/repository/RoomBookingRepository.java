package com.example.demo.repository;

import com.example.demo.entity.RoomBooking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomBookingRepository {

    private final List<RoomBooking> bookings = new ArrayList<>();

    public RoomBooking save(RoomBooking booking) {
        bookings.add(booking);
        return booking;
    }

    public List<RoomBooking> findAll() {
        return bookings;
    }
}
