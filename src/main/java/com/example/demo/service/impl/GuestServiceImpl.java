package com.example.demo.service.impl;

import com.example.demo.model.Guest;
import com.example.demo.repository.GuestRepository;
import com.example.demo.service.GuestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    // ⚠️ ONE constructor only
    public GuestServiceImpl(GuestRepository guestRepository,
                            PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Guest createGuest(Guest guest) {

        if (guest == null) throw new IllegalArgumentException("guest");
        if (guest.getEmail() == null || guest.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("email");

        if (guestRepository.existsByEmail(guest.getEmail()))
            throw new IllegalArgumentException("email");

        String raw = guest.getPassword() == null ? "" : guest.getPassword();
        guest.setPassword(passwordEncoder.encode(raw));

        if (guest.getActive() == null) guest.setActive(true);
        if (guest.getVerified() == null) guest.setVerified(false);
        if (guest.getRole() == null) guest.setRole("ROLE_USER");

        return guestRepository.save(guest);
    }

    // 🔑 MUST NOT throw (blank screen cause)
    @Override
    public Guest getGuestById(Long id) {
        if (id == null) return null;
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest updateGuest(Long id, Guest update) {
        if (id == null) return null;

        Guest g = guestRepository.findById(id).orElse(null);
        if (g == null) return null;

        if (update.getFullName() != null) g.setFullName(update.getFullName());
        if (update.getPhoneNumber() != null) g.setPhoneNumber(update.getPhoneNumber());
        if (update.getVerified() != null) g.setVerified(update.getVerified());
        if (update.getActive() != null) g.setActive(update.getActive());
        if (update.getRole() != null) g.setRole(update.getRole());

        return guestRepository.save(g);
    }

    @Override
    public void deactivateGuest(Long id) {
        if (id == null) return;
        Guest g = guestRepository.findById(id).orElse(null);
        if (g == null) return;

        g.setActive(false);
        guestRepository.save(g);
    }
}
