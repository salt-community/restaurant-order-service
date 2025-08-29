package com.example.restaurantorderservice.service;

import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderRequestDto req) {
        Order order = req.toOrder();
        orderRepository.save(order);
    }
}
