package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Guest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.GuestService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GuestService guestService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            GuestService guestService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        this.guestService = guestService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        Guest g = new Guest();

        g.setFullName(request.getFullName());
        g.setEmail(request.getEmail());
        g.setPhoneNumber(request.getPhoneNumber());

        
        if (request.getPassword() != null) {
            g.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            g.setPassword(passwordEncoder.encode(""));
        }

        
        String role = request.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER";
        } else if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }
        g.setRole(role);

        
        g.setVerified(true);
        g.setActive(true);

        guestService.createGuest(g);

        
        return "Registered Successfully";
    }

    
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        return jwtTokenProvider.generateToken(authentication);
    }
}
