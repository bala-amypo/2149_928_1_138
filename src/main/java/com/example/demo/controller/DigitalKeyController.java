package com.example.demo.model;

public class DigitalKey {

    private Long id;
    private String keyValue;

    public DigitalKey() {}

    public DigitalKey(Long id, String keyValue) {
        this.id = id;
        this.keyValue = keyValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
