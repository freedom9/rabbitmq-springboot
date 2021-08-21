package com.freedom.workqueue;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Channel channel = RabbitMqUtils.getChannel();

        System.out.println("输入发送信息：");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            final String message = scanner.next();
            if (message.equals("exit")) {
                break;
            }
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送成功： " + message);
        }

        System.out.println("程序退出！");
        System.exit(0);
    }
}
