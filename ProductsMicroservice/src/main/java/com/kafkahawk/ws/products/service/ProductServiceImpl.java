package com.kafkahawk.ws.products.service;
import com.kafkahawk.ws.core.ProductCreatedEvent;
import com.kafkahawk.ws.products.rest.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //initialize
    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    //if method is asynchronous
    // for sent method to wait until receive ack from broker, use the get method
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );
        LOGGER.info("**** Before publishing a product created event");

        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();
        LOGGER.info("Partition "+ result.getRecordMetadata().partition());
        LOGGER.info("**** Returning product id");
        return productId;
    }
}


//    public String createProduct(CreateProductRestModel productRestModel){
//        String productId = UUID.randomUUID().toString();
//        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
//                productId,
//                productRestModel.getTitle(),
//                productRestModel.getPrice(),
//                productRestModel.getQuantity());
//        //different ways to accept, can send record also
//        //topic name, message key, event object
//        //this sends a message to kafka topic synchronously and will not wait for acknowledgement
//        //kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent); // async method
//        //bad side about async is that if it fails to be persisted in kafkaTopic, the application wont know but u
//        //because in response, it will still receive product id
//
//        //synchronous method
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//        future.whenComplete((result, exception) -> {
//            if(exception !=null){
//                LOGGER.error("***** Failed to send message: " + exception.getMessage());
//            } else{
//                LOGGER.info("***** Message sent successfully: " + result.getRecordMetadata());
//                //getRecordMetadata : retrieve metadata associated with successfully sent message to kafka topic e.g which
//                //partition it used and offset, etc
//            }
//        });
//        future.join(); //this method will block the current thread until the future is complete //if got this code means ur code is synchronous
//        // and returns the result of computation
//
//        //if method is asynchronous
//        // for sent method to wait until receive ack from broker, use the get method
////        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();
//
//
//        LOGGER.info("***** Returning product id");
//        return productId;
//    }
//}
