package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarnQueueConsumer {

    @RabbitListener(queues = ConfirmConfig.WARN_QUEUE)
    private void receiveD(Message message, Channel channel) {
        final String msg = new String(message.getBody());
        log.info("报警发现不可路由信息：{}", msg);
    }
}
