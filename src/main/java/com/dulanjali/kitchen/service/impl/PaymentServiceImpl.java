package com.dulanjali.kitchen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.OrderDao;
import com.dulanjali.kitchen.dao.PaymentDao;
import com.dulanjali.kitchen.entities.Payment;
import com.dulanjali.kitchen.enums.PaymentStatus;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentDao paymentDao;
    private final OrderDao orderDao;

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentDao.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
    }

    @Override
    public Payment createPendingPayment(Long orderId) {
        var order = orderDao.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Payment payment = paymentDao.findByOrderId(orderId)
                .orElse(Payment.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .status(PaymentStatus.PENDING)
                        .build());

        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        log.info("Created/updated pending payment for order {}", orderId);
        return paymentDao.save(payment);
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentDao.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        payment.setStatus(status);
        log.info("Payment {} status changed to {}", paymentId, status);
        return paymentDao.save(payment);
    }
}
