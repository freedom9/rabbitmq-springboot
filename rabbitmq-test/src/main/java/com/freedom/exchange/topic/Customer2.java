package com.freedom.exchange.topic;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Customer2 {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        final String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("绑定的键： " + message.getEnvelope().getRoutingKey() + ", 消息： " + new String(message.getBody()));
        };

        System.out.println("C2等待接收消息...");
        channel.basicConsume(queueName, deliverCallback, consumerTag -> {});
    }
}
