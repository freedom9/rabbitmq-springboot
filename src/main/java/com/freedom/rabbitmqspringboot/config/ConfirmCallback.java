package com.freedom.rabbitmqspringboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (ack) {
            log.info("交换机已收到消息，id：{}", id);
        } else {
            log.warn("交换机未收到消息，id：{}, 错误原因：{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("退回的消息：{}，被交换机{}退回，退出原因：{}，路由key：{}", new String(returned.getMessage().getBody()),
                returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
    }
}
