// src/main/java/com/example/demo/service/RoomBookingService.java
package com.example.demo.service;

import com.example.demo.entity.RoomBooking;
import java.util.List;

public interface RoomBookingService {
    RoomBooking createBooking(RoomBooking booking);
    RoomBooking updateBooking(Long id, RoomBooking booking);
    RoomBooking getBookingById(Long id);
    List<RoomBooking> getAllBookings();
}
