package com.example.demo.controller;

import com.example.demo.model.Guest;
import com.example.demo.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @PostMapping
    public Guest create(@RequestBody Guest guest) {
        return service.create(guest);
    }

    @GetMapping
    public List<Guest> getAll() {
        return service.getAll();
    }
}
