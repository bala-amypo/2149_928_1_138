package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "digital_keys",
        uniqueConstraints = @UniqueConstraint(columnNames = "keyValue")
)
public class DigitalKey {

    // ✅ REQUIRED by portal & JPA
    public DigitalKey() {
        this.active = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomBooking booking;

    @Column(nullable = false, unique = true)
    private String keyValue;

    // ✅ Tests expect Instant
    private Instant issuedAt;
    private Instant expiresAt;

    private Boolean active = true;

    // ✅ Ensure safe defaults even if service not used
    @PrePersist
    public void onCreate() {
        if (active == null) {
            active = true;
        }
        if (issuedAt == null) {
            issuedAt = Instant.now();
        }
        if (keyValue == null) {
            keyValue = UUID.randomUUID().toString();
        }
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomBooking getBooking() {
        return booking;
    }

    public void setBooking(RoomBooking booking) {
        this.booking = booking;
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

    public Boolean getActive() {
        return active != null ? active : Boolean.TRUE;
    }

    public void setActive(Boolean active) {
        this.active = (active != null) ? active : Boolean.TRUE;
    }
}
