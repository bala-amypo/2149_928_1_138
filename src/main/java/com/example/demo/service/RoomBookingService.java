package com.example.demo.service;

import com.example.demo.model.RoomBooking;
import java.util.List;

public interface RoomBookingService {
    RoomBooking create(RoomBooking booking);
    List<RoomBooking> getAll();
}
