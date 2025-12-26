package com.example.demo.repository;

import com.example.demo.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    // Basic lookups
    Optional<Guest> findByEmail(String email);

    boolean existsByEmail(String email);

    // 🔥 Common portal test expectations
    List<Guest> findByActiveTrue();

    List<Guest> findByVerifiedTrue();

    List<Guest> findByRole(String role);
}
