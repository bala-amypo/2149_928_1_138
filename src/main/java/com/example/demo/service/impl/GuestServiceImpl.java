package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository repository;

    public GuestServiceImpl(GuestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Guest create(Guest guest) {
        return repository.save(guest);
    }

    @Override
    public Guest getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Guest> getAll() {
        return repository.findAll();
    }

    @Override
    public Guest update(Long id, Guest guest) {
        guest.setId(id);
        return repository.save(guest);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
