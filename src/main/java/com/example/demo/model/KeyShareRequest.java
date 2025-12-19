package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Guest sharedBy;

    @ManyToOne
    private Guest sharedWith;

    @ManyToOne
    private DigitalKey digitalKey;

    private LocalDateTime shareStart;
    private LocalDateTime shareEnd;

    private String status;

    @PrePersist
    public void onCreate() {
        this.status = "PENDING";
    }

    // getters & setters
    public Long getId() { return id; }

    public Guest getSharedBy() { return sharedBy; }
    public Guest getSharedWith() { return sharedWith; }

    public LocalDateTime getShareStart() { return shareStart; }
    public LocalDateTime getShareEnd() { return shareEnd; }

    public void setStatus(String status) { this.status = status; }
}
