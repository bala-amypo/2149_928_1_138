package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class GuestServiceImpl implements GuestService {

  private final GuestRepository guestRepository;
  private final PasswordEncoder passwordEncoder;

  public GuestServiceImpl(GuestRepository guestRepository, PasswordEncoder passwordEncoder) {
    this.guestRepository = guestRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Guest createGuest(Guest guest) {
    if (guestRepository.existsByEmail(guest.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }
    guest.setPassword(passwordEncoder.encode(guest.getPassword()));
    return guestRepository.save(guest);
  }

  @Override
  public Guest getGuestById(Long id) {
    return guestRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Guest not found " + id));
  }

  @Override
  public List < Guest > getAllGuests() {
    return guestRepository.findAll();
  }

  @Override
  public Guest updateGuest(Long id, Guest update) {
    Guest g = getGuestById(id);
    g.setFullName(update.getFullName());
    g.setPhoneNumber(update.getPhoneNumber());
    g.setVerified(update.getVerified());
    g.setActive(update.getActive());
    g.setRole(update.getRole());
    return guestRepository.save(g);
  }

  @Override
  public void deactivateGuest(Long id) {
    Guest g = getGuestById(id);
    g.setActive(false);
    guestRepository.save(g);
  }
}