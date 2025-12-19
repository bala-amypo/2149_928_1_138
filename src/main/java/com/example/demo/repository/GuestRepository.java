package com.example.demo.repository;

import com.example.demo.model.Guest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestRepository {

    private final List<Guest> guests = new ArrayList<>();

    public Guest save(Guest guest) {
        guests.add(guest);
        return guest;
    }

    public List<Guest> findAll() {
        return guests;
    }
}
