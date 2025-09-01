package com.example.restaurantorderservice.service;

import com.example.restaurantorderservice.dto.kafka.KafkaOrderDto;
import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.outbox.OutboxEvent;
import com.example.restaurantorderservice.outbox.OutboxRepository;
import com.example.restaurantorderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, KafkaOrderDto> kafkaTemplate;
    private final ObjectMapper mapper;

    public void createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        orderRepository.save(order);

        String payload;
        try {
             payload = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {

            //TODO change exception
            throw new RuntimeException(e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .orderId(order.getOrderId())
                .payload(payload)
                .createdAt(Instant.now())
                .processed(false)
            .build();

        outboxRepository.save(outboxEvent);
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
