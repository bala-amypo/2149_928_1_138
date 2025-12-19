package com.example.demo.service.impl;

import com.example.demo.entity.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository repository;

    public GuestServiceImpl(GuestRepository repository) {
        this.repository = repository;
    }

    public Guest createGuest(Guest guest) {
        return repository.save(guest);
    }

    public Guest updateGuest(Long id, Guest guest) {
        guest.setId(id);
        return repository.save(guest);
    }

    public Guest getGuestById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Guest> getAllGuests() {
        return repository.findAll();
    }

    public void deactivateGuest(Long id) {
        Guest g = repository.findById(id).orElse(null);
        if (g != null) {
            g.setActive(false);
            repository.save(g);
        }
    }
}
