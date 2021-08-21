package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.DelayedQueueConfig;
import com.freedom.rabbitmqspringboot.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable("message") String message) {
        log.info("time：{}, 发送一条消息给两个ttl队列：{}", LocalDateTime.now(), message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XB", "消息来自ttl为30s的队列：" + message);

        return "发送成功";
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttl}")
    public String sendExpirationMsg(@PathVariable("message") String message, @PathVariable("ttl") String ttl) {
        log.info("time：{}, 发送一条时长{}毫秒ttl消息给队列：{}", LocalDateTime.now(), ttl, message);

        rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, "XC", message, msg -> {
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });

        return "发送成功";
    }

    @GetMapping("/sendDelayMsg/{message}/{ttl}")
    public String sendDelayMsg(@PathVariable("message") String message, @PathVariable("ttl") Integer ttl) {
        log.info("time：{}, 发送一条时长{}毫秒ttl消息给队列：{}", LocalDateTime.now(), ttl, message);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            msg.getMessageProperties().setDelay(ttl);
            return msg;
        });

        return "发送成功";
    }
}
