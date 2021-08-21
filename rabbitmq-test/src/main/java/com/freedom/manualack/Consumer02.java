package com.freedom.manualack;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02 {

    private static final String QUEUE_NAME = "manual_ack";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("消费消息： " + new String(message.getBody()));

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 手动应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        });

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };

//        channel.basicQos(5);

        System.out.println("C2等待接收消息，处理消息较快慢...");
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
