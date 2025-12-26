package com.example.demo.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "digital_keys",
        uniqueConstraints = @UniqueConstraint(columnNames = "keyValue")
)
public class DigitalKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private RoomBooking booking;

    @Column(nullable = false, unique = true)
    private String keyValue;

    private Instant issuedAt;

    private Instant expiresAt;

    private boolean active = true;

    public Long getId() {
        return id;
    }

    public RoomBooking getBooking() {
        return booking;
    }

    public void setBooking(RoomBooking booking) {
        this.booking = booking;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyValue() {
        return keyValue;
    }
    
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
    
    public Instant getIssuedAt() {
        return issuedAt;
    }
    
    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
