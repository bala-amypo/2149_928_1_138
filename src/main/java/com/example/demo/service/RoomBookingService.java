package com.example.demo.service;

import com.example.demo.entity.RoomBooking;
import java.util.List;

public interface RoomBookingService {
    RoomBooking create(RoomBooking booking);
    List<RoomBooking> getAll();
}
