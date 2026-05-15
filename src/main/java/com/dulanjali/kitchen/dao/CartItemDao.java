package com.dulanjali.kitchen.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dulanjali.kitchen.entities.CartItem;

@Repository
public interface CartItemDao extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndFoodItemId(Long cartId, Long foodItemId);
}
