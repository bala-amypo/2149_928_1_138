package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Share start time is required")
    @Column(nullable = false)
    private LocalDateTime shareStart;

    @NotNull(message = "Share end time is required")
    @Column(nullable = false)
    private LocalDateTime shareEnd;

    @NotNull(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "Digital key is required")
    @ManyToOne
    @JoinColumn(name = "digital_key_id", nullable = false)
    private DigitalKey digitalKey;

    @NotNull(message = "Shared by guest is required")
    @ManyToOne
    @JoinColumn(name = "shared_by", nullable = false)
    private Guest sharedBy;

    @NotNull(message = "Shared with guest is required")
    @ManyToOne
    @JoinColumn(name = "shared_with", nullable = false)
    private Guest sharedWith;

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getShareStart() {
        return shareStart;
    }

    public void setShareStart(LocalDateTime shareStart) {
        this.shareStart = shareStart;
    }

    public LocalDateTime getShareEnd() {
        return shareEnd;
    }

    public void setShareEnd(LocalDateTime shareEnd) {
        this.shareEnd = shareEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
