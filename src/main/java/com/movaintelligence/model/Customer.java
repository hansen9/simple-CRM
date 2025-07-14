package com.movaintelligence.model;

public class Customer {
    private Long id;
    private String nama;
    private Tipe tipe;
    private Status status;
    // Address dan ContactPerson akan direlasikan kemudian

    public enum Tipe {
        PERORANGAN, PERUSAHAAN
    }

    public enum Status {
        SEED, NURTURE, LEAD, ACTIVE, LOYAL
    }

    // Constructor, getter, setter
    public Customer() {}

    public Customer(Long id, String nama, Tipe tipe, Status status) {
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Tipe getTipe() {
        return tipe;
    }

    public void setTipe(Tipe tipe) {
        this.tipe = tipe;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

