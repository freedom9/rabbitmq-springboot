package com.freedom.exchange.direct;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producer {

    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入日志类型（info, warn, error）: ");
            String logType = scanner.next();
            if ("exit".equals(logType)) {
                break;
            }
            System.out.print("输入发送的内容：");
            final String message = scanner.next();

            channel.basicPublish(EXCHANGE_NAME, logType, null, message.getBytes());
        }

        System.out.println("程序退出！");
        System.exit(0);
    }
}
