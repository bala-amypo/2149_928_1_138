package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest updateGuest(Long id, Guest guest) {
        guest.setId(id);
        return guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
}
