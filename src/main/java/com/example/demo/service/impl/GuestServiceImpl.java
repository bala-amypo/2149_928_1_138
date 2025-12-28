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

    public GuestServiceImpl(
            GuestRepository guestRepository,
            PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Guest createGuest(Guest guest) {

        if (guest == null)
            throw new IllegalArgumentException("guest required");

        if (guest.getEmail() == null || guest.getEmail().isBlank())
            throw new IllegalArgumentException("email required");

        if (guestRepository.existsByEmail(guest.getEmail()))
            throw new IllegalArgumentException("email already exists");

        if (guest.getPassword() != null &&
                !guest.getPassword().startsWith("$2a$")) {
            guest.setPassword(passwordEncoder.encode(guest.getPassword()));
        }

        if (guest.getActive() == null) guest.setActive(true);
        if (guest.getVerified() == null) guest.setVerified(false);
        if (guest.getRole() == null) guest.setRole("ROLE_USER");

        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuestById(Long id) {
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

        Guest existing = guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        if (update.getFullName() != null)
            existing.setFullName(update.getFullName());

        if (update.getPhoneNumber() != null)
            existing.setPhoneNumber(update.getPhoneNumber());

        if (update.getVerified() != null)
            existing.setVerified(update.getVerified());

        if (update.getActive() != null)
            existing.setActive(update.getActive());

        if (update.getRole() != null)
            existing.setRole(update.getRole());

        return guestRepository.save(existing);
    }

    @Override
    public void deactivateGuest(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        guest.setActive(false);
        guestRepository.save(guest);
    }
}
