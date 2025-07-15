package com.movaintelligence.simplecrm.entity;

public class Address {
    private Long id;
    private String street;
    private String city;
    private String province;
    private String country;
    private Long customerId;

    public Address() {}

    public Address(Long id, String street, String city, String province, String country, Long customerId) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}

