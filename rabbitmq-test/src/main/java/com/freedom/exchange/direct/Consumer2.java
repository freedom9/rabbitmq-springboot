package com.freedom.exchange.direct;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer2 {

    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        final String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "error");

        DeliverCallback deliverCallback = (customerTag, message) -> {
            System.out.println("接收的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = customerTag -> {
            System.out.println("接收消息被中断");
        };

        System.out.println("等待接收消息...");
        channel.basicConsume(queueName, deliverCallback, cancelCallback);
    }
}
