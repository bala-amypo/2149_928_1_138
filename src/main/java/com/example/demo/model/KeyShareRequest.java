package com.example.demo.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "key_share_requests")
public class KeyShareRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private DigitalKey digitalKey;

  @ManyToOne
  private Guest sharedBy;

  @ManyToOne
  private Guest sharedWith;

  private Timestamp shareStart;
  private Timestamp shareEnd;

  private String status = "PENDING";

  private Timestamp createdAt;

  public KeyShareRequest() {}

  @PrePersist
  public void onCreate() {
    this.createdAt = new Timestamp(System.currentTimeMillis());
  }

  // getters and setters
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

  public Timestamp getShareStart() {
    return shareStart;
  }
  public void setShareStart(Timestamp shareStart) {
    this.shareStart = shareStart;
  }

  public Timestamp getShareEnd() {
    return shareEnd;
  }
  public void setShareEnd(Timestamp shareEnd) {
    this.shareEnd = shareEnd;
  }

  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }
}