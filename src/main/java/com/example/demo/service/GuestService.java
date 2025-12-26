package com.example.demo.service;

import com.example.demo.model.Guest;
import java.util.List;

public interface GuestService {

    Guest createGuest(Guest guest);

    Guest getGuestById(Long id);

    List<Guest> getAllGuests();

    Guest updateGuest(Long id, Guest guest);

    void deactivateGuest(Long id);
}
