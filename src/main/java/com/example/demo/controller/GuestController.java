package com.example.demo.controller;

import com.example.demo.entity.Guest;
import com.example.demo.service.GuestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @PostMapping
    public Guest create(@RequestBody Guest guest) {
        return service.createGuest(guest);
    }

    @GetMapping("/{id}")
    public Guest get(@PathVariable Long id) {
        return service.getGuestById(id);
    }

    @GetMapping
    public List<Guest> getAll() {
        return service.getAllGuests();
    }

    @PutMapping("/{id}")
    public Guest update(@PathVariable Long id, @RequestBody Guest guest) {
        return service.updateGuest(id, guest);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateGuest(id);
    }
}
