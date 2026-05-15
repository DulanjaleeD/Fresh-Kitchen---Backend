package com.dulanjali.kitchen.service;

import java.util.List;

import com.dulanjali.kitchen.entities.secure.User;

public interface UserService {
    User createUser(User user);
    User updateUser(String userId, User user);
    User getUserById(String userId);
    List<User> getAllUsers();
    void deleteUser(String userId);
}
