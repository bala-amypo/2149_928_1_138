package com.example.demo.service;

import com.example.demo.entity.Guest;
import java.util.List;

public interface GuestService {
    Guest create(Guest guest);
    List<Guest> getAll();
}
