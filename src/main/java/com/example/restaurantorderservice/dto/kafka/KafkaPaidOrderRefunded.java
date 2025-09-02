package com.example.restaurantorderservice.dto.kafka;

import com.example.restaurantorderservice.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record KafkaPaidOrderRefunded(
    UUID eventId, // unique id for the message
    UUID paymentId, // Payment.id
    String orderId,
    BigDecimal amount,
    String providerPaymentId,
    PaymentStatus status, // AUTHORIZED | FAILED | REFUNDED
    String failureReason, // null unless FAILED
    Instant createdAt, // original payment timestamp
    Instant occurredAt // when we produced the event
) {}
