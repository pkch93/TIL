package edu.pkch.tester;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "pkch")
public class Receiver {

    @RabbitHandler
    public void receive(String message) {
        System.out.println("[receive] " + message);
    }
}
