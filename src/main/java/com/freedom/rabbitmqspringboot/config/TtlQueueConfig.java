package com.freedom.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TtlQueueConfig {

    public static final String EXCHANGE_X = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";

    public static final String EXCHANGE_Y = "Y";
    public static final String QUEUE_D = "QD";

    @Bean
    public DirectExchange exchangeX() {
        return ExchangeBuilder.directExchange(EXCHANGE_X).build();
    }

    @Bean
    public DirectExchange exchangeY() {
        return new DirectExchange(EXCHANGE_Y);
    }

    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                .deadLetterExchange(EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .ttl(10000)
                .build();
    }

    @Bean
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .deadLetterExchange(EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .ttl(30000)
                .build();
    }

    @Bean
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                .deadLetterExchange(EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean
    public Queue queueD() {
        return QueueBuilder.durable(QUEUE_D)
                .build();
    }

    @Bean
    public Binding queueAExchangeX(Queue queueA, DirectExchange exchangeX){
        return BindingBuilder.bind(queueA).to(exchangeX).with("XA");
    }

    @Bean
    public Binding queueBExchangeX(Queue queueB, DirectExchange exchangeX){
        return BindingBuilder.bind(queueB).to(exchangeX).with("XB");
    }

    @Bean
    public Binding queueCExchangeX(Queue queueC, DirectExchange exchangeX){
        return BindingBuilder.bind(queueC).to(exchangeX).with("XC");
    }

    @Bean
    public Binding queueDExchangeY(Queue queueD, DirectExchange exchangeY){
        return BindingBuilder.bind(queueD).to(exchangeY).with("YD");
    }
}
