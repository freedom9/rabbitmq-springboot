package com.freedom.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriorityConfig {

    public static final String PRIORITY_EXCHANGE = "priority.exchange";
    public static final String PRIORITY_QUEUE = "priority.queue";
    public static final String PRIORITY_ROUTING_KEY = "priority.routing.key";

    @Bean
    public DirectExchange priorityExchange() {
        return ExchangeBuilder.directExchange(PRIORITY_EXCHANGE).build();
    }

    @Bean
    public Queue priorityQueue() {
        return QueueBuilder.durable(PRIORITY_QUEUE)
                .maxPriority(10)
                .build();
    }

    @Bean
    public Binding bindPriorityQueue(Queue priorityQueue, Exchange priorityExchange) {
        return BindingBuilder.bind(priorityQueue).to(priorityExchange).with(PRIORITY_ROUTING_KEY).noargs();
    }
}
