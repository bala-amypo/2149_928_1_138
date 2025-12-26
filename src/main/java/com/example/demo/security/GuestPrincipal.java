package com.example.demo.security;

import com.example.demo.model.Guest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class GuestPrincipal implements UserDetails {

    private final Guest guest;

    public GuestPrincipal(Guest guest) {
        this.guest = guest;
    }

    public Long getId() {
        return guest.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = guest.getRole();

        // 🔐 Ensure Spring Security role format
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return guest.getPassword();
    }

    @Override
    public String getUsername() {
        return guest.getEmail(); // email-based login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return guest.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return guest.isVerified();
    }
}
