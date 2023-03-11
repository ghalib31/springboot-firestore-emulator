package com.group9.firestore.document;

public class Address {
    private String streetName;
    private int houseNumber;
    private String zipCode;
    private String city;

    public String getStreetName() {
        return streetName;
    }

    public Address setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public Address setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }
}
