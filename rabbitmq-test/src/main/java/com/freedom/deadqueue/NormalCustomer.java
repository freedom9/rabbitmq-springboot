package com.freedom.deadqueue;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class NormalCustomer {

    private final static String NORMAL_EXCHANGE = "normal_exchange";

    private final static String DEAD_EXCHANGE = "dead_exchange";

    private final static String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();

        Map<String, Object> params = new HashMap<>();
        // 正常队列设置死信交换机，key为固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 正常队列设置死信routing-key，key为固定值
        params.put("x-dead-letter-routing-key", "dead");
        // 设置正常队列长度的限制
//        params.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normal");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            final String msg = new String(message.getBody());
            // 模拟拒收消息
            if ("info3".equals(msg)) {
                System.out.println("接收的消息：" + message + "，并拒绝签收该消息");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("接收的消息： " + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        });

        System.out.println("normal customer等待接收消息...");
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {});
    }
}
