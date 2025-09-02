package com.example.restaurantorderservice.service;

//import com.example.restaurantorderservice.dto.kafka.KafkaOrderDto;

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

//    @KafkaListener(id = "myId", topics = "orders")
//    public void listen(ConsumerRecord<String, String> record) {
//        // injecting ConsumerRecord in case we need message metadata
//        // otherwise can just inject a String
//        String value = record.value();
//        try {
//            KafkaOrderDto dto = mapper.readValue(value, KafkaOrderDto.class);
//            Order order = dto.toOrder();
//            System.out.println("order id = " + order.getOrderId());
//            System.out.println("order status = " + order.getOrderStatus());
//            orderRepository.save(order);
//        } catch (JsonProcessingException e) {
//
//            System.out.println("EXCEPTION DESERIALIZING MESSAGE!");
//            e.printStackTrace();
//        }
//    }
}
