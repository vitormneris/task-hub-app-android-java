package com.codecrafters.taskhub.service;

import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.request.FindByEmailRequest;
import com.codecrafters.taskhub.request.InsertRequest;
import com.codecrafters.taskhub.request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserService {
//    public User findById(String id) {
//        User user = null;
//
//        try {
//            FindByIdRequest findByIdRequest = new FindByIdRequest();
//            String jsonString = findByIdRequest.execute(id).get();
//
//            if (jsonString == null) return null;
//
//            user = new User();
//            user = convertToUser(jsonString);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return user;
//    }

    public User findByEmail(String email) {
        User user = null;

        try {
            FindByEmailRequest findByEmailRequest = new FindByEmailRequest();
            String jsonString = findByEmailRequest.execute(email).get();

            if (jsonString == null) return null;

            user = new User();
            user = convertToUser(jsonString);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String insert(User user) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InsertRequest insertRequest = new InsertRequest();
            return insertRequest.execute(objectMapper.writeValueAsString(user)).get();
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "false";
    }

//    public boolean update(String id, User user){
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            UpdateRequest updateRequest = new UpdateRequest();
//            return updateRequest.execute(id, objectMapper.writeValueAsString(user)).get();
//        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean deleteById(String id){
//        try {
//            DeleteRequest deleteRequest = new DeleteRequest();
//            return deleteRequest.execute(id).get();
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public Boolean login(String email, String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            LoginRequest loginRequest = new LoginRequest();
            String login = loginRequest.execute(objectMapper.writeValueAsString(user)).get();
            if (login == null) return null;
            return Boolean.valueOf(login);
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private User convertToUser(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();

        try {
            Map map = objectMapper.readValue(jsonString, Map.class);
            user.setId((String) map.get("id"));
            user.setName((String) map.get("name"));
            user.setEmail((String) map.get("email"));
            user.setPassword((String) map.get("password"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
