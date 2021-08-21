package com.freedom.helloworld;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

public class Producer {

    public static final String QUEUE_NAME = "hello_world";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World!";

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送完毕");
    }
}
