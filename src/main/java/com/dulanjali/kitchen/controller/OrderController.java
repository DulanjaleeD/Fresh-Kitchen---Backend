package com.dulanjali.kitchen.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dulanjali.kitchen.entities.Order;
import com.dulanjali.kitchen.enums.OrderStatus;
import com.dulanjali.kitchen.securityConfig.CurrentUserProvider;
import com.dulanjali.kitchen.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.checkout(userId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        String adminId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, adminId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        String adminId = currentUserProvider.getCurrentUserId();
        return ResponseEntity.ok(orderService.getAllOrders(adminId));
    }
}
