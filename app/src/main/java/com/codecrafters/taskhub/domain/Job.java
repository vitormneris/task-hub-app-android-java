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
    private User crafter;
    private Set<User> subscribers;

}
