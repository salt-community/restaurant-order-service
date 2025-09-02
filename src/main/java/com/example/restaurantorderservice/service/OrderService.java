package com.example.restaurantorderservice.service;

import com.example.restaurantorderservice.dto.kafka.KafkaPaidOrderAuthorizedDto;
import com.example.restaurantorderservice.dto.kafka.KafkaPaidOrderFailedDto;
import com.example.restaurantorderservice.dto.kafka.KafkaPaidOrderRefunded;
import com.example.restaurantorderservice.dto.kafka.KafkaMessageDto;
import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.exception.custom.JsonMapperException;
import com.example.restaurantorderservice.exception.custom.NotFoundException;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.outbox.OutboxEvent;
import com.example.restaurantorderservice.outbox.OutboxRepository;
import com.example.restaurantorderservice.repository.ItemRepository;
import com.example.restaurantorderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final OutboxRepository outboxRepository;

//    private final KafkaTemplate<String, KafkaOrderDto> kafkaTemplate;

    private final ObjectMapper mapper;

    private String mapToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonMapperException(e.getMessage());
        }
    }

    private void logJsonMappingError(Exception e) {
        System.out.println("EXCEPTION DESERIALIZING MESSAGE!");
        System.out.println("e.getClass() = " + e.getClass());
        System.out.println("e.getMessage() = " + e.getMessage());
        System.out.println("e.getCause() = " + e.getCause());
        e.printStackTrace();
    }

    @Transactional
    public UUID createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        orderRepository.save(order);

        itemRepository.saveAll(order.getItems());

        String payload = mapToJson(
            KafkaMessageDto.fromOrder(order)
        );

        OutboxEvent outboxEvent = OutboxEvent.builder()
            .orderId(order.getOrderId())
            .payload(payload)
            .createdAt(Instant.now())
            .processed(false)
            .build();

        outboxRepository.save(outboxEvent);

        return order.getOrderId();
    }

    public Order getOrder(UUID orderId) {
        return getOrderById(orderId);
    }

    public void removeOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        orderRepository.delete(order);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository
            .findById(orderId)
            .orElseThrow(() ->
                new NotFoundException(
                    "Order with Id: %s not found".formatted(orderId)
                )
            );
    }

    @KafkaListener(id = "myId", topics = "TOPIC_AUTHORIZED")
    public void listenPaidOrderAuthorized(ConsumerRecord<String, String> record) {
        String value = record.value();
        try {
            KafkaPaidOrderAuthorizedDto dto = mapper.readValue(value, KafkaPaidOrderAuthorizedDto.class);
        } catch (JsonProcessingException e) {
            logJsonMappingError(e);
        }
    }

    @KafkaListener(id = "myId", topics = "TOPIC_FAILED")
    public void listenPaidOrderFailed(ConsumerRecord<String, String> record) {
        String value = record.value();
        try {
            KafkaPaidOrderFailedDto dto = mapper.readValue(value, KafkaPaidOrderFailedDto.class);
        } catch (JsonProcessingException e) {
            logJsonMappingError(e);
        }
    }

    @KafkaListener(id = "myId", topics = "TOPIC_REFUND")
    public void listenPaidOrderRefund(ConsumerRecord<String, String> record) {
        String value = record.value();
        try {
            KafkaPaidOrderRefunded dto = mapper.readValue(value, KafkaPaidOrderRefunded.class);
        } catch (JsonProcessingException e) {
            logJsonMappingError(e);
        }
    }

//    @Builder
//    public record PaymentEvent (
//        UUID eventId, // unique id for the message
//        UUID paymentId, // Payment.id
//        String orderId,
//        BigDecimal amount,
//        String providerPaymentId,
//        PaymentStatus status, // AUTHORIZED | FAILED | REFUNDED
//        String failureReason, // null unless FAILED
//        Instant createdAt, // original payment timestamp
//        Instant occurredAt // when we produced the event
//    ){
//    }
}
