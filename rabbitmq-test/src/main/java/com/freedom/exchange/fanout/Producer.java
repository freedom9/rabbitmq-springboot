package com.freedom.exchange.fanout;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        // 定义交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        System.out.println("请输入发送的内容：");
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String message = scanner.next();
            if ("exit".equals(message)) {
                break;
            }
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        }

        System.out.println("程序退出！");
        System.exit(0);
    }
}
