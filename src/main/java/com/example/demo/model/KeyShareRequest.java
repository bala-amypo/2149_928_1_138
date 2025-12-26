package com.example.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private DigitalKey digitalKey;

    @ManyToOne(optional = false)
    private Guest sharedBy;

    @ManyToOne(optional = false)
    private Guest sharedWith;

    private Instant shareStart;
    private Instant shareEnd;

    // ✅ ENUM — NOT STRING
    @Enumerated(EnumType.STRING)
    private ShareStatus status;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = ShareStatus.PENDING; // ✅ enum, not string
        }
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
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

    public ShareStatus getStatus() {
        return status;
    }

    public void setStatus(ShareStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
