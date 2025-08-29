package com.example.restaurantorderservice.config;

import com.example.restaurantorderservice.dto.kafka.KafkaOrderDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private final ObjectMapper mapper;
    private final OrderRepository orderRepository;

    public KafkaConfig(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.mapper = new ObjectMapper();
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

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("orders")
            .partitions(10)
            .replicas(1)
            .build();
    }
}
