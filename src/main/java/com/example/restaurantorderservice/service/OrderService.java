package com.example.restaurantorderservice.service;

import com.example.restaurantorderservice.dto.kafka.KafkaOrderDto;
import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, KafkaOrderDto> kafkaTemplate;
    private final ObjectMapper mapper;

    public void createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        orderRepository.save(order);
        kafkaTemplate.send("orders", KafkaOrderDto.fromOrder(order));
    }

    @KafkaListener(id = "myId", topics = "orders")
    public void listen(ConsumerRecord<String, String> record) {
        // injecting ConsumerRecord in case we need message metadata
        // otherwise can just inject a String
        String value = record.value();
        try {
            KafkaOrderDto dto = mapper.readValue(value, KafkaOrderDto.class);
            Order order = dto.toOrder();
            System.out.println("order id = " + order.getOrderId());
            System.out.println("order status = " + order.getOrderStatus());
            orderRepository.save(order);
        } catch (JsonProcessingException e) {

            System.out.println("EXCEPTION DESERIALIZING MESSAGE!");
            e.printStackTrace();
        }
    }
}
