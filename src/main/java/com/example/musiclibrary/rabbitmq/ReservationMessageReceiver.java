package com.example.musiclibrary.rabbitmq;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class ReservationMessageReceiver {
    private CountDownLatch latch = new CountDownLatch(1);
    private final SimpMessagingTemplate messagingTemplate;
    public ReservationMessageReceiver(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void receiveMessage(String message) {
        System.out.println("Получено из reservation-queue <" + message + ">");
        messagingTemplate.convertAndSend("/topic/reservation", message);
        latch.countDown();
    }
    public CountDownLatch getLatch() {
        return latch;
    }
}