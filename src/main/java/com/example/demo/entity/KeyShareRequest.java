package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "key_share_request")
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime shareStart;
    private LocalDateTime shareEnd;
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    private DigitalKey digitalKey;

    @ManyToOne(cascade = CascadeType.ALL)
    private Guest sharedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    private Guest sharedWith;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getShareStart() { return shareStart; }
    public void setShareStart(LocalDateTime shareStart) { this.shareStart = shareStart; }
    public LocalDateTime getShareEnd() { return shareEnd; }
    public void setShareEnd(LocalDateTime shareEnd) { this.shareEnd = shareEnd; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public DigitalKey getDigitalKey() { return digitalKey; }
    public void setDigitalKey(DigitalKey digitalKey) { this.digitalKey = digitalKey; }
    public Guest getSharedBy() { return sharedBy; }
    public void setSharedBy(Guest sharedBy) { this.sharedBy = sharedBy; }
    public Guest getSharedWith() { return sharedWith; }
    public void setSharedWith(Guest sharedWith) { this.sharedWith = sharedWith; }
}
