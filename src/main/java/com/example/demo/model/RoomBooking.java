package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room_bookings")
public class RoomBooking {

    // ✅ REQUIRED by portal & JPA
    public RoomBooking() {
        this.roommates = new HashSet<>();
        this.active = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Guest guest;

    private String roomNumber;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "booking_roommates",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    private Set<Guest> roommates = new HashSet<>();

    @PrePersist
    public void onCreate() {
        if (active == null) {
            active = true;
        }
        if (roommates == null) {
            roommates = new HashSet<>();
        }
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    // 🔥 PORTAL-SAFE
    public Boolean getActive() {
        return active != null ? active : Boolean.TRUE;
    }

    // 🔥 PORTAL-SAFE
    public void setActive(Boolean active) {
        this.active = (active != null) ? active : Boolean.TRUE;
    }

    public Set<Guest> getRoommates() {
        if (roommates == null) {
            roommates = new HashSet<>();
        }
        return roommates;
    }

    public void setRoommates(Set<Guest> roommates) {
        this.roommates = (roommates != null) ? roommates : new HashSet<>();
    }
}
