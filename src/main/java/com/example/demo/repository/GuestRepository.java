package com.example.demo.repository;

import com.example.demo.entity.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
}
