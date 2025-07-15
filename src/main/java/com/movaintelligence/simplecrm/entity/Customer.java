package com.movaintelligence.simplecrm.entity;

import java.util.List;

public class Customer {
    private Long id;
    private String nama;
    private Type type;
    private Status status;
    private List<Address> addresses; // Added field for one-to-many relationship with Address

    public enum Type {
        PERORANGAN, PERUSAHAAN
    }

    public enum Status {
        SEED, NURTURE, LEAD, ACTIVE, LOYAL
    }

    // Constructor, getter, setter
    public Customer() {}

    public Customer(Long id, String nama, Type tipe, Status status) {
        this.id = id;
        this.nama = nama;
        this.type = tipe;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return nama;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type tipe) {
        this.type = tipe;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
