package com.example.demo.model;

pblic class RoomBooking {

    private Long id;
    private String roomNumber;
    private String status;

    public RoomBooking() {}

    public RoomBooking(Long id, String roomNumber, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
