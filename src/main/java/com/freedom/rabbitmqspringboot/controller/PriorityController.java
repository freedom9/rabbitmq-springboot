package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.PriorityConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/priority")
@RestController
public class PriorityController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/test")
    public String test() {
        for (int i = 0; i < 10; i++) {
            int priority = i % 3;
            rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE, PriorityConfig.PRIORITY_ROUTING_KEY,"test-" + i, messagePostProcessor -> {
                messagePostProcessor.getMessageProperties().setPriority(priority);
                return messagePostProcessor;
            });
        }

        return "发送成功";
    }
}
