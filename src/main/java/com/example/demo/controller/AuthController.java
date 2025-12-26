package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.model.Guest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.GuestService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final GuestService guestService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            GuestService guestService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.guestService = guestService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        Guest guest = new Guest();
        guest.setFullName(request.getFullName());
        guest.setEmail(request.getEmail());
        guest.setPhoneNumber(request.getPhoneNumber());
        guest.setPassword(passwordEncoder.encode(request.getPassword()));
        guest.setRole("ROLE_USER");
        guest.setVerified(true);
        guest.setActive(true);

        guestService.createGuest(guest);

        return ResponseEntity.ok("User registered successfully");
    }

    // ================= LOGIN =================
   @PostMapping("/login")
public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    String token = jwtTokenProvider.generateToken(authentication);

    UserDetails userDetails =
            (UserDetails) authentication.getPrincipal();

    Long userId = 1L; // temporary
    String email = userDetails.getUsername();
    String role = userDetails.getAuthorities()
                             .iterator()
                             .next()
                             .getAuthority();

    return ResponseEntity.ok(
            new TokenResponse(token, userId, email, role)
    );
}

}
