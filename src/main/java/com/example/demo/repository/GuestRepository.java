package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
