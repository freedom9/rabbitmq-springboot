package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.ConfirmCallback;
import com.freedom.rabbitmqspringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConfirmCallback confirmCallback;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(confirmCallback);
    }

    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable("message") String message) {
        CorrelationData correlationData = new CorrelationData("1");
        String routingKey = ConfirmConfig.CONFIRM_ROUTING_KEY;
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, routingKey, message + "—" + routingKey, correlationData);

        correlationData = new CorrelationData("2");
        routingKey = routingKey + "1";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, routingKey, message + "—" + routingKey, correlationData);

        return "发送成功";
    }
}
