package com.example.restaurantorderservice.domain.service;

import com.example.restaurantorderservice.domain.enums.OrderStatus;
import com.example.restaurantorderservice.domain.model.Order;
import com.example.restaurantorderservice.domain.repository.ItemRepository;
import com.example.restaurantorderservice.domain.repository.OrderRepository;
import com.example.restaurantorderservice.exception.custom.JsonMapperException;
import com.example.restaurantorderservice.exception.custom.NotFoundException;
import com.example.restaurantorderservice.http.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.messaging.eventDto.KafkaMessageDto;
import com.example.restaurantorderservice.messaging.outbox.OutboxEvent;
import com.example.restaurantorderservice.messaging.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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

    private final ObjectMapper mapper;

    @Transactional
    public UUID createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        System.out.println("order = " + order);
        orderRepository.save(order);
        itemRepository.saveAll(order.getItems());
        System.out.println("order.getItems() = " + order.getItems());

        System.out.println("order = " + order);


        buildOrderEvent(order, "order.created.v1");

        return order.getOrderId();
    }

    public Order getOrder(UUID orderId) {
        return getOrderById(orderId);
    }

    public void removeOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        orderRepository.delete(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);

        if (order.getOrderStatus() == OrderStatus.CANCELED) return;

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        buildOrderEvent(order, "order.canceled.v1");
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

    private void buildOrderEvent(Order order, String topic) {
        String payload = mapToJson(
            KafkaMessageDto.fromOrder(order)
        );
        outboxRepository.save(OutboxEvent.builder()
            .eventId(UUID.randomUUID())
            .topic(topic)
            .orderId(order.getOrderId())
            .payload(payload)
            .createdAt(Instant.now())
            .processed(false)
            .build());
    }

    private String mapToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonMapperException(e.getMessage());
        }
    }

}
