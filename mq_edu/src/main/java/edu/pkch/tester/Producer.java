package edu.pkch.tester;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Producer {
    private static int sharedNumber = 1;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 2000, initialDelay = 1000)
    public void produce() {
        String message = "hello pkch" + sharedNumber;
        sharedNumber += 1;
        rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println("[produce] " + message);
    }
}
