package com.freedom.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String CONFIRM_ROUTING_KEY = "confirm";

    public static final String BACKUP_EXCHANGE = "backup.exchange";
    public static final String BACKUP_QUEUE = "backup.queue";
    public static final String WARN_QUEUE = "warn.queue";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding bindConfirmQueue(Queue confirmQueue, Exchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY).noargs();
    }

    @Bean
    public FanoutExchange backupExchange() {
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warnQueue() {
        return QueueBuilder.durable(WARN_QUEUE).build();
    }

    @Bean
    public Binding bindBackupQueue(Queue backupQueue, Exchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange).with("").noargs();
    }


    @Bean
    public Binding bindWarnQueue(Queue warnQueue, Exchange backupExchange) {
        return BindingBuilder.bind(warnQueue).to(backupExchange).with("").noargs();
    }
}
