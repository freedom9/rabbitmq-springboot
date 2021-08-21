package com.freedom.exchange.fanout;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        // 生成一个临时队列
        final String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("C2等待接收消息...");

        DeliverCallback deliverCallback = (customerTag, message) -> {
            System.out.println("接收的消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = customerTag -> {
            System.out.println("接收消息被中断");
        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
