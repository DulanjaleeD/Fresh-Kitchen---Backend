package com.dulanjali.kitchen.service;

import com.dulanjali.kitchen.entities.Cart;

public interface CartService {
    Cart getCartByUserId(String userId);
    Cart addItemToCart(String userId, Long foodItemId, Integer quantity);
    Cart updateCartItemQuantity(String userId, Long cartItemId, Integer quantity);
    Cart removeCartItem(String userId, Long cartItemId);
    Cart clearCart(String userId);
}
