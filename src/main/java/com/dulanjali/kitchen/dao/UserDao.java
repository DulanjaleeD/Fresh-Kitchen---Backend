package com.dulanjali.kitchen.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulanjali.kitchen.entities.secure.User;

@Repository
public interface UserDao extends JpaRepository<User, String>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
