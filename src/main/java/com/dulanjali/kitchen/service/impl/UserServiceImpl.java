package com.dulanjali.kitchen.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.entities.secure.User;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        if (userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Creating user {}", user.getEmail());
        return userDao.save(user);
    }

    @Override
    public User updateUser(String userId, User user) {
        User existing = getUserById(userId);
        if (!existing.getEmail().equals(user.getEmail()) && userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        log.info("Updating user {}", userId);
        return userDao.save(existing);
    }

    @Override
    public User getUserById(String userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void deleteUser(String userId) {
        User existing = getUserById(userId);
        log.info("Deleting user {}", userId);
        userDao.delete(existing);
    }
}
