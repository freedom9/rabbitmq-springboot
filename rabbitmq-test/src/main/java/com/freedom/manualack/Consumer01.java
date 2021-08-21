package com.freedom.manualack;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer01 {

    private static final String QUEUE_NAME = "manual_ack";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("消费消息： " + new String(message.getBody()));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 手动应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        });

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };

        // 功能一：不公平分发。所有的消费者prefetchCount等于1才会是不公平分发，不公平分发表现为能者多劳
        // 功能二：预取值。prefetchCont表示预取多少个。预期值万了之后又是安装轮询方式下方
//        channel.basicQos(2);

        System.out.println("C3等待接收消息，处理消息较快...");
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
