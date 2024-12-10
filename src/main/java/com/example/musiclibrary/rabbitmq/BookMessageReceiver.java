package com.example.musiclibrary.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Component
public class BookMessageReceiver {
    private CountDownLatch latch = new CountDownLatch(1);
    private final SimpMessagingTemplate messagingTemplate;

    public BookMessageReceiver(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void receiveMessage(String message) {
        System.out.println("Получено из book-queue <" + message + ">");
        messagingTemplate.convertAndSend("/topic/book", message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}