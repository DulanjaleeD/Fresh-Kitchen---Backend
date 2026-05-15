package com.dulanjali.kitchen.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulanjali.kitchen.entities.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
