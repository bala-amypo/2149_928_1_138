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

    public Guest create(Guest guest) {
        return repository.save(guest);
    }

    public List<Guest> getAll() {
        return repository.findAll();
    }
}
