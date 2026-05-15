package com.dulanjali.kitchen.service;

import java.util.List;

import com.dulanjali.kitchen.entities.Order;
import com.dulanjali.kitchen.enums.OrderStatus;

public interface OrderService {
    Order checkout(String userId);
    Order updateOrderStatus(Long orderId, OrderStatus nextStatus, String adminId);
    List<Order> getOrdersForUser(String userId);
    List<Order> getAllOrders(String adminId);
}
