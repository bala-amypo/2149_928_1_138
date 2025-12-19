package com.example.demo.model;

public class AccessLog {

    private Long id;
    private String action;

    public AccessLog() {}

    public AccessLog(Long id, String action) {
        this.id = id;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
