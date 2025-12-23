package com.example.demo.controller;

import com.example.demo.entity.Guest;
import com.example.demo.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    
    @PostMapping
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        Guest savedGuest = guestService.createGuest(guest);
        return new ResponseEntity<>(savedGuest, HttpStatus.CREATED);
    }

   
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody Guest guest) {
        return ResponseEntity.ok(guestService.updateGuest(id, guest));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok("Guest deleted successfully");
    }
}
