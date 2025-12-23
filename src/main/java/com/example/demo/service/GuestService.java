package com.example.demo.service;

import com.example.demo.entity.Guest;

import java.util.List;

public interface GuestService {

    Guest createGuest(Guest guest);

    List<Guest> getAllGuests();

    Guest getGuestById(Long id);

    Guest updateGuest(Long id, Guest guest);

    void deleteGuest(Long id);
}
