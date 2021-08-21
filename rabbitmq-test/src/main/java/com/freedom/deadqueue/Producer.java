package com.freedom.deadqueue;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class Producer {

    private final static String NORMAL_EXCHANGE = "normal_exchange";

    private final static String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 设置消息ttl时间
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 1; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "normal", null, message.getBytes());
            System.out.println("发送消息： " + message);
        }
    }
}
