package com.example.musiclibrary.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class RentalMessageReceiver {
    private CountDownLatch latch = new CountDownLatch(1);
    private final SimpMessagingTemplate messagingTemplate;
    public RentalMessageReceiver(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void receiveMessage(String message) {
        System.out.println("Получено из rental-queue <" + message + ">");
        messagingTemplate.convertAndSend("/topic/rental", message);
        latch.countDown();
    }
    public CountDownLatch getLatch() {
        return latch;
    }
}