package com.example.restaurantorderservice.messaging;

import com.example.restaurantorderservice.messaging.eventDto.PaymentFailedDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
@ActiveProfiles("test")
public class ConsumerTest {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    PaymentFailedListenerTestExtender consumer;

    @Disabled
    @Test
    void shouldConsumeTopic_whenListeningToIt() throws InterruptedException {
        //Arrange
        String topic = "${app.topic.payment.failed}";
        String payload = """
            {
            "eventId": "08243e59-a0c1-4af4-9b3b-855e7b5d3431",
            "orderId": "08243e67-a0c1-4af4-9b3b-855e7b5d3431",
            "paymentStatus": "FAILED"
            }
            """;

        //Act
        kafkaTemplate.send(topic, payload);

        //Assert
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        PaymentFailedDto data = PaymentFailedDto.builder()
            .eventId(UUID.fromString("08243e59-a0c1-4af4-9b3b-855e7b5d3431"))
            .orderId("08243e67-a0c1-4af4-9b3b-855e7b5d3431")
            .paymentStatus("FAILED")
            .build();
        assertTrue(messageConsumed);
        assertEquals(consumer.getPayload(), data);

    }
}
