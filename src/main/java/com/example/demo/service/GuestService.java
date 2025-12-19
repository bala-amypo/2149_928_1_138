package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Guest;

public interface GuestService {

    Guest create(Guest guest);

    Guest getById(Long id);

    List<Guest> getAll();

    Guest update(Long id, Guest guest);

    void delete(Long id);
}
