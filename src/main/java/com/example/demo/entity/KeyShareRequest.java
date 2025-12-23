package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime shareStart;

    @NotNull
    @Future
    private LocalDateTime shareEnd;

    @NotBlank
    private String status;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "digital_key_id", nullable = false)
    private DigitalKey digitalKey;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "shared_by_id", nullable = false)
    private Guest sharedBy;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "shared_with_id", nullable = false)
    private Guest sharedWith;

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
