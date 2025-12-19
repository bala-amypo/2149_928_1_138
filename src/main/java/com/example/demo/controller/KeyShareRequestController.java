package com.example.demo.model;

public class KeyShareRequest {

    private Long id;
    private Long digitalKeyId;
    private Long guestId;

    public KeyShareRequest() {}

    public KeyShareRequest(Long id, Long digitalKeyId, Long guestId) {
        this.id = id;
        this.digitalKeyId = digitalKeyId;
        this.guestId = guestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDigitalKeyId() {
        return digitalKeyId;
    }

    public void setDigitalKeyId(Long digitalKeyId) {
        this.digitalKeyId = digitalKeyId;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
}
