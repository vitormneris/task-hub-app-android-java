package com.codecrafters.taskhub.domain;

public class Address {
    private String state;
    private String city;
    private String neighborhood;
    private String complement;

    public Address() {
    }

    public Address(String state, String city, String neighborhood, String complement) {
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.complement = complement;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    @Override
    public String toString() {
        return "Address{" +
                "state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", complement='" + complement + '\'' +
                '}';
    }
}
