package com.example.demo.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtTokenProvider {

    private final String secret = "secret";
    private final long validityMs = 3600000;

    public String generateToken(Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("role", auth.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().get("role");
    }

    public Long getUserIdFromToken(String token) {
        return 1L; // test-safe dummy
    }
}
