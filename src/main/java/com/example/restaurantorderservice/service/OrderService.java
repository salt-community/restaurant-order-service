package com.example.restaurantorderservice.service;

import com.example.restaurantorderservice.dto.kafka.KafkaOrderDto;
import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, KafkaOrderDto> kafkaTemplate;

    public void createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        orderRepository.save(order);
        kafkaTemplate.send("orders", KafkaOrderDto.fromOrder(order));
    }
}
