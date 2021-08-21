package com.freedom.rabbitmqspringboot.controller;

import com.freedom.rabbitmqspringboot.config.PriorityConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriorityConsumer {

    @RabbitListener(queues = PriorityConfig.PRIORITY_QUEUE)
    public void receive(Message message, Channel channel) {
        log.info("接收的信息的优先级：{}，内容：{}", message.getMessageProperties().getPriority(), new String(message.getBody()));
    }
}
