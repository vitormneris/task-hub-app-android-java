package com.codecrafters.taskhub.domain;

import java.util.Set;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Set<Job> jobsCreated;
    private Set<Job> jobsSubscribed;

    public User() {
    }

    public User(String id, String name, String email, String phone, String password, Set<Job> jobsCreated, Set<Job> jobsSubscribed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.jobsCreated = jobsCreated;
        this.jobsSubscribed = jobsSubscribed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Job> getJobsCreated() {
        return jobsCreated;
    }

    public void setJobsCreated(Set<Job> jobsCreated) {
        this.jobsCreated = jobsCreated;
    }

    public Set<Job> getJobsSubscribed() {
        return jobsSubscribed;
    }

    public void setJobsSubscribed(Set<Job> jobsSubscribed) {
        this.jobsSubscribed = jobsSubscribed;
    }
}
