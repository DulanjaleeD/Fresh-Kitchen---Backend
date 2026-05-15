package com.dulanjali.kitchen.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulanjali.kitchen.entities.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
}
