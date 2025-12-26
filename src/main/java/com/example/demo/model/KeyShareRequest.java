package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

    // ✅ REQUIRED by portal & JPA
    public KeyShareRequest() {
        this.status = "PENDING";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DigitalKey digitalKey;

    @ManyToOne
    private Guest sharedBy;

    @ManyToOne
    private Guest sharedWith;

    private Instant shareStart;
    private Instant shareEnd;

    private String status = "PENDING";

    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    // ======================
    // Getters & Setters
    // ======================

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

    public Guest getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(Guest sharedBy) {
        this.sharedBy = sharedBy;
    }

    public Guest getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Guest sharedWith) {
        this.sharedWith = sharedWith;
    }

    public Instant getShareStart() {
        return shareStart;
    }

    public void setShareStart(Instant shareStart) {
        this.shareStart = shareStart;
    }

    public Instant getShareEnd() {
        return shareEnd;
    }

    public void setShareEnd(Instant shareEnd) {
        this.shareEnd = shareEnd;
    }

    public String getStatus() {
        return status != null ? status : "PENDING";
    }

    public void setStatus(String status) {
        this.status = (status != null) ? status : "PENDING";
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // ✅ REQUIRED for portal tests
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
