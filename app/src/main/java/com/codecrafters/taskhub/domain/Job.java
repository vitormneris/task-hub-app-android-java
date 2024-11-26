package com.codecrafters.taskhub.domain;

import java.util.Set;

public class Job {
    private String id;
    private String title;
    private String moment;
    private String details;
    private String imageUrl;
    private Double payment;
    private Boolean available;
    private String address;
    private User crafter;
    private Set<User> subscribers;

    public Job() {
    }

    public Job(String id, String title, String moment, String details, String imageUrl, Double payment, Boolean available, String address, User crafter) {
        this.id = id;
        this.title = title;
        this.moment = moment;
        this.details = details;
        this.imageUrl = imageUrl;
        this.payment = payment;
        this.available = available;
        this.address = address;
        this.crafter = crafter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getCrafter() {
        return crafter;
    }

    public void setCrafter(User crafter) {
        this.crafter = crafter;
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", moment='" + moment + '\'' +
                ", details='" + details + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", payment=" + payment +
                ", available=" + available +
                ", address=" + address +
                ", crafter=" + crafter +
                ", subscribers=" + subscribers +
                '}';
    }
}
