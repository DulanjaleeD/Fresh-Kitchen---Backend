package com.dulanjali.kitchen.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulanjali.kitchen.entities.OrderItem;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
}
