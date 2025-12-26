package com.example.demo.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "digital_keys", uniqueConstraints = @UniqueConstraint(columnNames = "keyValue"))
public class DigitalKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private RoomBooking booking;

  @Column(unique = true)
  private String keyValue;

  private Timestamp issuedAt;
  private Timestamp expiresAt;

  private Boolean active = true;

  public DigitalKey() {}

  // getters and setters
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

  public Timestamp getIssuedAt() {
    return issuedAt;
  }
  public void setIssuedAt(Timestamp issuedAt) {
    this.issuedAt = issuedAt;
  }

  public Timestamp getExpiresAt() {
    return expiresAt;
  }
  public void setExpiresAt(Timestamp expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Boolean getActive() {
    return active;
  }
  public void setActive(Boolean active) {
    this.active = active;
  }
}