package com.codecrafters.taskhub.service;

import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.request.jobs.UpdateAvailableJobRequest;
import com.codecrafters.taskhub.request.users.DeleteUserRequest;
import com.codecrafters.taskhub.request.users.FindByEmailUserRequest;
import com.codecrafters.taskhub.request.users.FindByIdUserRequest;
import com.codecrafters.taskhub.request.users.InsertUserRequest;
import com.codecrafters.taskhub.request.users.LoginUserRequest;
import com.codecrafters.taskhub.request.users.SubscribeUserRequest;
import com.codecrafters.taskhub.request.users.UnsubscribeUserRequest;
import com.codecrafters.taskhub.request.users.UpdateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutionException;

public class UserService {
    public User findById(String id) {
        User user = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FindByIdUserRequest findByIdUserRequest = new FindByIdUserRequest();
            String jsonString = findByIdUserRequest.execute(id).get();

            if (jsonString == null) return null;

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            user = objectMapper.readValue(jsonString, User.class);

        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FindByEmailUserRequest findByEmailUserRequest = new FindByEmailUserRequest();
            String jsonString = findByEmailUserRequest.execute(email).get();

            if (jsonString == null) return null;

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            user = objectMapper.readValue(jsonString, User.class);

        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String insert(User user) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InsertUserRequest insertUserRequest = new InsertUserRequest();
            return insertUserRequest.execute(objectMapper.writeValueAsString(user)).get();
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public String update(String id, User user){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            UpdateUserRequest updateRequest = new UpdateUserRequest();
            return updateRequest.execute(id, objectMapper.writeValueAsString(user)).get();
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public String subscribe(String userId, String jobId){
        try {
            SubscribeUserRequest subscribeUserRequest = new SubscribeUserRequest();
            return subscribeUserRequest.execute(userId, jobId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return "false";
    }

    public boolean unsubscribe(String userId, String jobId){
        try {
            UnsubscribeUserRequest unsubscribeUserRequest = new UnsubscribeUserRequest();
            return unsubscribeUserRequest.execute(userId, jobId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String id){
        try {
            DeleteUserRequest deleteRequest = new DeleteUserRequest();
            return deleteRequest.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean login(String email, String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            LoginUserRequest loginUserRequest = new LoginUserRequest();
            String login = loginUserRequest.execute(objectMapper.writeValueAsString(user)).get();
            if (login == null) return null;
            return Boolean.valueOf(login);
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
