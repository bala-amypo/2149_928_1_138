package com.example.demo.security;

import com.example.demo.repository.GuestRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final GuestRepository guestRepository;

    public CustomUserDetailsService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Username is empty");
        }

        var guest = guestRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        // ✅ ROLE SAFETY (REQUIRED BY TESTS)
        String role = guest.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER";
        }

        // ✅ PASSWORD SAFETY (REQUIRED BY SPRING SECURITY)
        String password = guest.getPassword();
        if (password == null) {
            password = "";
        }

        return new User(
                guest.getEmail(),
                password,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
// package com.example.demo.security;

// import com.example.demo.repository.GuestRepository;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.*;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     private final GuestRepository guestRepository;

//     public CustomUserDetailsService(GuestRepository guestRepository) {
//         this.guestRepository = guestRepository;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String username)
//             throws UsernameNotFoundException {

//         if (username == null || username.trim().isEmpty()) {
//             throw new UsernameNotFoundException("User not found");
//         }

//         var guest = guestRepository.findByEmail(username)
//                 .orElseThrow(() ->
//                         new UsernameNotFoundException("User not found")
//                 );

//         String role = guest.getRole() == null ? "ROLE_USER" : guest.getRole();
//         String password = guest.getPassword() == null ? "" : guest.getPassword();

//         return new User(
//                 guest.getEmail(),
//                 password,
//                 List.of(new SimpleGrantedAuthority(role))
//         );
//     }
// }
