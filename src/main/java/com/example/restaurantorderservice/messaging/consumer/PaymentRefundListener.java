package com.example.restaurantorderservice.messaging.consumer;


import com.example.restaurantorderservice.domain.service.OrderService;
import com.example.restaurantorderservice.messaging.eventDto.PaymentRefundDto;
import com.example.restaurantorderservice.messaging.idempotent.ConsumedEvent;
import com.example.restaurantorderservice.messaging.idempotent.ConsumedEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentRefundListener {

    private final ObjectMapper mapper;

    private final OrderService orderService;

    private final ConsumedEventRepository consumedEventRepository;

    @KafkaListener(id = "myId3", topics = "${app.topic.payment.refund}")
    public void onPaymentRefund(ConsumerRecord<String, String> record) {
        PaymentRefundDto dto = readPaymentRefund(record.value());
        UUID eventId = dto.eventId();

        //Idempotency
        if (consumedEventRepository.existsById(eventId)) return;

        UUID orderId = UUID.fromString(dto.orderId());
        orderService.cancelOrder(orderId);

        ConsumedEvent event = new ConsumedEvent(eventId, Instant.now());
        consumedEventRepository.save(event);
    }

    private PaymentRefundDto readPaymentRefund(String json) {
        try {
            return mapper.readValue(json, PaymentRefundDto.class);
        } catch (JsonProcessingException e) {
            logJsonMappingError(e);
            throw new RuntimeException("Failed to parse PaymentRefundDto", e);
        }
    }

    private void logJsonMappingError(Exception e) {
        System.out.println("EXCEPTION DESERIALIZING MESSAGE!");
        System.out.println("e.getClass() = " + e.getClass());
        System.out.println("e.getMessage() = " + e.getMessage());
        System.out.println("e.getCause() = " + e.getCause());
        e.printStackTrace();
    }
}
