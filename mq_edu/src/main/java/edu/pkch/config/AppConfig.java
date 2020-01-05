package edu.pkch.config;

import edu.pkch.tester.Producer;
import edu.pkch.tester.Receiver;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    public Queue queue() {
        return new Queue("pkch");
    }

    @Profile("producer")
    @Bean
    public Producer producer() {
        return new Producer();
    }

    @Profile("receiver")
    @Bean
    public Receiver receiver() {
        return new Receiver();
    }
}
