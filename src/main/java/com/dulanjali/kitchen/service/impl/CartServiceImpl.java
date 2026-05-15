package com.dulanjali.kitchen.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.CartDao;
import com.dulanjali.kitchen.dao.CartItemDao;
import com.dulanjali.kitchen.dao.FoodItemDao;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.entities.Cart;
import com.dulanjali.kitchen.entities.CartItem;
import com.dulanjali.kitchen.enums.FoodStatus;
import com.dulanjali.kitchen.exception.InvalidOperationException;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.service.CartService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final FoodItemDao foodItemDao;
    private final UserDao userDao;

    @Override
    public Cart getCartByUserId(String userId) {
        return findOrCreateCart(userId);
    }

    @Override
    public Cart addItemToCart(String userId, Long foodItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = findOrCreateCart(userId);
        var food = foodItemDao.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found: " + foodItemId));

        if (food.getStatus() != FoodStatus.AVAILABLE) {
            throw new InvalidOperationException("Food item is currently out of stock");
        }

        CartItem cartItem = cartItemDao.findByCartIdAndFoodItemId(cart.getId(), foodItemId)
                .orElse(CartItem.builder()
                        .cart(cart)
                        .foodItem(food)
                        .quantity(0)
                        .build());

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemDao.save(cartItem);
        log.info("Added food {} x{} to cart {}", foodItemId, quantity, cart.getId());

        return cartDao.findById(cart.getId()).orElse(cart);
    }

    @Override
    public Cart updateCartItemQuantity(String userId, Long cartItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = findOrCreateCart(userId);
        CartItem item = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new InvalidOperationException("Cart item does not belong to this user");
        }

        item.setQuantity(quantity);
        cartItemDao.save(item);
        log.info("Updated cart item {} quantity to {}", cartItemId, quantity);
        return cartDao.findById(cart.getId()).orElse(cart);
    }

    @Override
    public Cart removeCartItem(String userId, Long cartItemId) {
        Cart cart = findOrCreateCart(userId);
        CartItem item = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new InvalidOperationException("Cart item does not belong to this user");
        }

        cartItemDao.delete(item);
        log.info("Removed cart item {} from cart {}", cartItemId, cart.getId());
        return cartDao.findById(cart.getId()).orElse(cart);
    }

    @Override
    public Cart clearCart(String userId) {
        Cart cart = findOrCreateCart(userId);
        cart.getItems().clear();
        log.info("Cleared cart {}", cart.getId());
        return cartDao.save(cart);
    }

    private Cart findOrCreateCart(String userId) {
        return cartDao.findByUserId(userId).orElseGet(() -> {
            var user = userDao.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
            Cart cart = Cart.builder().user(user).items(new ArrayList<>()).build();
            log.info("Creating cart for user {}", userId);
            return cartDao.save(cart);
        });
    }
}
