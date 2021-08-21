package com.freedom.deadqueue;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class DeadCustomer {

    private final static String DEAD_EXCHANGE = "dead_exchange";

    private final static String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        final Channel channel = RabbitMqUtils.getChannel();

        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "dead");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("接收的消息：" + new String(message.getBody()));
        });

        System.out.println("dead customer等待接收消息...");
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});
    }
}
