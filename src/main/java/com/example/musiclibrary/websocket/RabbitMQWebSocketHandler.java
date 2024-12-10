package com.example.musiclibrary.websocket;

import com.example.musiclibrary.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQWebSocketHandler {
    private final SimpMessagingTemplate messagingTemplate;

    public RabbitMQWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.queueUser)
    public void handleUserQueue(String message) {
        messagingTemplate.convertAndSend("/topic/user", message);
    }

    @RabbitListener(queues = RabbitMQConfig.queueBook)
    public void handleBookQueue(String message) {
        messagingTemplate.convertAndSend("/topic/book", message);
    }

    @RabbitListener(queues = RabbitMQConfig.queueRental)
    public void handleRentalQueue(String message) {
        messagingTemplate.convertAndSend("/topic/rental", message);
    }

    @RabbitListener(queues = RabbitMQConfig.queueReservation)
    public void handleReservationQueue(String message) {
        messagingTemplate.convertAndSend("/topic/reservation", message);
    }
}
