package com.kafkahawk.ws.emailnotification.handler;

import com.kafkahawk.ws.core.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {
//    @KafkaListener(topics={"topic1","topic2"})
//handle event of different types in the same class then need to put kafkalistener on top before the class
    //so instead of defining it above in every method of the class can just put all the way ontop
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        LOGGER.info("Received a new event: " + productCreatedEvent.getTitle());
    }
}
