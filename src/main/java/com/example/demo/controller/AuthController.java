package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.model.Guest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.GuestService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final GuestService guestService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            GuestService guestService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.guestService = guestService;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (guestService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }

        Guest guest = new Guest();
        guest.setFullName(request.getFullName());
        guest.setEmail(request.getEmail());
        guest.setPhoneNumber(request.getPhoneNumber());

        // ✅ RAW password only (ANY length allowed)
        guest.setPassword(request.getPassword());

        guest.setRole("ROLE_USER");
        guest.setVerified(true);
        guest.setActive(true);

        // ✅ password will be encoded INSIDE service
        guestService.createGuest(guest);

        return ResponseEntity.ok("User registered successfully");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // ✅ CORRECT TOKEN GENERATION
            String token = jwtTokenProvider.generateToken(authentication);

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(
                    new TokenResponse(
                            token,
                            1L, // temp userId
                            userDetails.getUsername(),
                            userDetails.getAuthorities()
                                       .iterator()
                                       .next()
                                       .getAuthority()
                    )
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }
}
