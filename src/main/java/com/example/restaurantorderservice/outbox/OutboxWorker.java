package com.example.restaurantorderservice.outbox;

import com.example.restaurantorderservice.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxWorker {

    private final OutboxRepository outboxRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void publishOutboxEvents() {
        
        List<OutboxEvent> outboxEvents = outboxRepository.findAllUnprocessed();
        // OutboxEvents -> Orders -> KafkaMessages -> Json string
        outboxEvents.stream()
            .map(outboxEvent -> {
//                    objectMapper.readValue(outboxEvent.getPayload(), Order.class)
                    return new Order();
                }
            )
            .map(KafkaMessage::fromOrder)
            .map(kafkaMessage -> {
                return "SOME JSON STRING";
            })
            .toList();

        for (OutboxEvent event: outboxEvents) {
            System.out.println("eveneeeeeeeeeeet = " + event);
            kafkaTemplate.send("orders", event.getPayload());
            event.setProcessed(true);
            System.out.println("eveneeeeeeeeeeet = " + event);
            outboxRepository.save(event);
        }

    }
}
