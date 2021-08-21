package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.DelayedQueueConfig;
import com.freedom.rabbitmqspringboot.config.TtlQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = {TtlQueueConfig.QUEUE_D, DelayedQueueConfig.DELAYED_QUEUE})
    private void receiveD(Message message, Channel channel) {
        final String msg = new String(message.getBody());
        log.info("time：{}, 收到死信队列信息：{}", LocalDateTime.now(), msg);
    }
}
