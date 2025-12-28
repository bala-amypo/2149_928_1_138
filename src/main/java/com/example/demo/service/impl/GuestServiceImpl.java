package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    public GuestServiceImpl(GuestRepository guestRepository,
                            PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Guest createGuest(Guest guest) {

        if (guest == null) {
            throw new IllegalArgumentException("Guest must not be null");
        }

        if (guest.getEmail() == null || guest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        // ✅ REQUIRED BY TESTS
        if (guestRepository.existsByEmail(guest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // ✅ PASSWORD MUST ALWAYS BE ENCODED
        String rawPassword = guest.getPassword();
        if (rawPassword == null) {
            rawPassword = "";
        }
        if (!rawPassword.startsWith("$2a$")) {
            guest.setPassword(passwordEncoder.encode(rawPassword));
        }

        if (guest.getActive() == null) {
            guest.setActive(true);
        }

        if (guest.getVerified() == null) {
            guest.setVerified(false);
        }

        if (guest.getRole() == null || guest.getRole().isBlank()) {
            guest.setRole("ROLE_USER");
        }

        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuestById(Long id) {

        // ✅ IMPORTANT: null ID must also fail
        if (id == null) {
            throw new ResourceNotFoundException("Guest not found");
        }

        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest updateGuest(Long id, Guest update) {

        Guest existing = getGuestById(id);

        // ❌ EMAIL IS IMMUTABLE

        if (update.getFullName() != null) {
            existing.setFullName(update.getFullName());
        }

        if (update.getPhoneNumber() != null) {
            existing.setPhoneNumber(update.getPhoneNumber());
        }

        if (update.getVerified() != null) {
            existing.setVerified(update.getVerified());
        }

        if (update.getActive() != null) {
            existing.setActive(update.getActive());
        }

        if (update.getRole() != null && !update.getRole().isBlank()) {
            existing.setRole(update.getRole());
        }

        return guestRepository.save(existing);
    }

    @Override
    public void deactivateGuest(Long id) {
        Guest guest = getGuestById(id);
        guest.setActive(false);
        guestRepository.save(guest);
    }
}
