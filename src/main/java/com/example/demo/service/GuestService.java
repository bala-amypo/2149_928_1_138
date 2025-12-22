package com.example.demo.service;

import com.example.demo.entity.Guest;
import java.util.List;

public interface GuestService {
    Guest save(Guest guest);
    List<Guest> getAll();
}
