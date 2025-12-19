package com.example.demo.service;

import com.example.demo.entity.RoomBooking;
import java.util.List;

public interface RoomBookingService {
    RoomBooking createBooking(RoomBooking booking);
    RoomBooking updateBooking(Long id, RoomBooking booking);
    RoomBooking getBooking(Long id);
    List<RoomBooking> getAllBookings();
    void deactivateBooking(Long id);
}
