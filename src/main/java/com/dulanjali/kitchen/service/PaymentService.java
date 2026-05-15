package com.dulanjali.kitchen.service;

import com.dulanjali.kitchen.entities.Payment;
import com.dulanjali.kitchen.enums.PaymentStatus;

public interface PaymentService {
    Payment getPaymentByOrderId(Long orderId);
    Payment createPendingPayment(Long orderId);
    Payment updatePaymentStatus(Long paymentId, PaymentStatus status);
}
