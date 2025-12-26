package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "access_logs")
public class AccessLog {

    // ✅ REQUIRED by portal & JPA
    public AccessLog() {
        this.result = "DENIED";
        this.reason = "unknown";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DigitalKey digitalKey;

    @ManyToOne
    private Guest guest;

    // ✅ Tests expect Instant
    private Instant accessTime;

    private String result;
    private String reason;

    // ✅ Ensure safe defaults even if service layer is bypassed
    @PrePersist
    public void onCreate() {
        if (this.accessTime == null) {
            this.accessTime = Instant.now();
        }
        if (this.result == null) {
            this.result = "DENIED";
        }
        if (this.reason == null) {
            this.reason = "unknown";
        }
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DigitalKey getDigitalKey() {
        return digitalKey;
    }

    public void setDigitalKey(DigitalKey digitalKey) {
        this.digitalKey = digitalKey;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Instant getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Instant accessTime) {
        this.accessTime = accessTime;
    }

    public String getResult() {
        return result != null ? result : "DENIED";
    }

    public void setResult(String result) {
        this.result = (result != null) ? result : "DENIED";
    }

    public String getReason() {
        return reason != null ? reason : "unknown";
    }

    public void setReason(String reason) {
        this.reason = (reason != null) ? reason : "unknown";
    }
}
