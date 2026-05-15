package com.dulanjali.kitchen.service.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.CartDao;
import com.dulanjali.kitchen.dao.FoodItemDao;
import com.dulanjali.kitchen.dao.OrderDao;
import com.dulanjali.kitchen.dao.PaymentDao;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.entities.Order;
import com.dulanjali.kitchen.entities.OrderItem;
import com.dulanjali.kitchen.entities.Payment;
import com.dulanjali.kitchen.enums.FoodStatus;
import com.dulanjali.kitchen.enums.OrderStatus;
import com.dulanjali.kitchen.enums.PaymentStatus;
import com.dulanjali.kitchen.enums.Role;
import com.dulanjali.kitchen.exception.InvalidOperationException;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.exception.UnauthorizedActionException;
import com.dulanjali.kitchen.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private static final Map<OrderStatus, EnumSet<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PLACED, EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED),
            OrderStatus.PREPARING, EnumSet.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED),
            OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class),
            OrderStatus.CANCELLED, EnumSet.noneOf(OrderStatus.class));

    private final OrderDao orderDao;
    private final CartDao cartDao;
    private final FoodItemDao foodItemDao;
    private final PaymentDao paymentDao;
    private final UserDao userDao;

    @Override
    public Order checkout(String userId) {
        var user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        var cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new InvalidOperationException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new InvalidOperationException("Cart is empty");
        }

        Order order = Order.builder()
                .status(OrderStatus.PLACED)
                .totalAmount(0.0)
                .user(user)
                .build();

        double total = 0.0;
        for (var cartItem : cart.getItems()) {
            var food = cartItem.getFoodItem();
            if (food.getStatus() == FoodStatus.OUT_OF_STOCK) {
                throw new InvalidOperationException("Food item out of stock: " + food.getName());
            }

            if (food.getStockQuantity() < cartItem.getQuantity()) {
                throw new InvalidOperationException("Insufficient stock for item: " + food.getName());
            }

            food.setStockQuantity(food.getStockQuantity() - cartItem.getQuantity());
            if (food.getStockQuantity() <= 0) {
                food.setStatus(FoodStatus.OUT_OF_STOCK);
            }
            foodItemDao.save(food);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .foodItem(food)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(food.getPrice())
                    .build();
            order.getOrderItems().add(orderItem);

            double lineTotal = orderItem.getUnitPrice() * orderItem.getQuantity();
            total += lineTotal;
        }

        order.setTotalAmount(total);
        Order savedOrder = orderDao.save(order);

        Payment payment = Payment.builder()
                .amount(total)
                .status(PaymentStatus.PENDING)
                .order(savedOrder)
                .build();
        paymentDao.save(payment);
        savedOrder.setPayment(payment);

        cart.getItems().clear();
        cartDao.save(cart);

        log.info("Created order {} from cart {}", savedOrder.getId(), cart.getId());
        return savedOrder;
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus nextStatus, String adminId) {
        validateAdmin(adminId);
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        EnumSet<OrderStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(order.getStatus(), EnumSet.noneOf(OrderStatus.class));
        if (!allowed.contains(nextStatus)) {
            throw new InvalidOperationException("Invalid status transition from " + order.getStatus() + " to " + nextStatus);
        }

        order.setStatus(nextStatus);
        log.info("Order {} status changed to {}", orderId, nextStatus);
        return orderDao.save(order);
    }

    @Override
    public List<Order> getOrdersForUser(String userId) {
        return orderDao.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders(String adminId) {
        validateAdmin(adminId);
        return orderDao.findAll();
    }

    private void validateAdmin(String userId) {
        var user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admins can perform this operation");
        }
    }
}
