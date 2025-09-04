package com.example.restaurantorderservice.messaging.eventDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentFailedDto(
    UUID eventId, // unique id for the message
    UUID paymentId, // Payment.id
    String orderId,
    BigDecimal amount,
    String providerPaymentId,
    String paymentStatus, // AUTHORIZED | FAILED | REFUNDED
    String failureReason, // null unless FAILED
    Instant createdAt, // original payment timestamp
    Instant occurredAt // when we produced the event
) {
}
