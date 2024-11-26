package com.codecrafters.taskhub.service;

import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.request.jobs.DeleteJobRequest;
import com.codecrafters.taskhub.request.jobs.FindAllJobRequest;
import com.codecrafters.taskhub.request.jobs.FindByIdJobRequest;
import com.codecrafters.taskhub.request.jobs.InsertJobRequest;
import com.codecrafters.taskhub.request.jobs.UpdateAvailableJobRequest;
import com.codecrafters.taskhub.request.jobs.UpdateJobRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class JobService {

    public Job findById(String id) {
        Job job = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FindByIdJobRequest findByIdJobRequest = new FindByIdJobRequest();
            String jsonString = findByIdJobRequest.execute(id).get();

            if (jsonString == null) return null;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            job = objectMapper.readValue(jsonString, Job.class);
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return job;
    }

    public List<Job> findAll() {
        List<Job> jobs = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FindAllJobRequest findAllJobRequest = new FindAllJobRequest();
            String jsonString = findAllJobRequest.execute().get();

            if (jsonString == null) return null;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jobs = objectMapper.readValue(jsonString, new TypeReference<List<Job>>() {});

        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public boolean insert(Job job){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InsertJobRequest insertJobRequest = new InsertJobRequest();
            return insertJobRequest.execute(objectMapper.writeValueAsString(job)).get();
        } catch (ExecutionException | InterruptedException | JsonProcessingException  e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String id, Job job) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UpdateJobRequest updateRequest = new UpdateJobRequest();
            return updateRequest.execute(id, objectMapper.writeValueAsString(job)).get();
        } catch (ExecutionException | InterruptedException | JsonProcessingException  e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAvailable(String id) {
        try {
            UpdateAvailableJobRequest updateAvailableJobRequest = new UpdateAvailableJobRequest();
            return updateAvailableJobRequest.execute(id).get();
        } catch (ExecutionException | InterruptedException  e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String id) {
        try {
            DeleteJobRequest deleteRequest = new DeleteJobRequest();
            return deleteRequest.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
