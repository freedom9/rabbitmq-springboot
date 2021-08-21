package com.freedom.releaseconfirm;

import com.freedom.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ReleaseConfirm {

    private static final String QUEUE_NAME = "release_confirm";

    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        final Instant startTime = Instant.now();
        // 33834 30899 28714
//        singleConfirm();
        // 1900 1993 2056
//        batchConfirm();
        // 922 916 812
        asyncConfirm();
        System.out.printf("所用时间： %s\n", Duration.between(startTime, Instant.now()).toMillis());
    }

    private static void singleConfirm() throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        // 开启发布确认
        channel.confirmSelect();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, String.valueOf(i).getBytes());
            // 服务端返回false或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
    }

    private static void batchConfirm() throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        channel.confirmSelect();

        int batchCount = 100;
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, String.valueOf(i).getBytes());
            if (i % batchCount == 0 || i == MESSAGE_COUNT) {
                System.out.println(i);
                channel.waitForConfirms();
            }
        }
    }

    private static void asyncConfirm() throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        channel.confirmSelect();

        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirms = outstandingConfirms.headMap(deliveryTag, true);
                confirms.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.printf("未确认的消息：%s\n", deliveryTag);
        };

        // 消息监听器
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            outstandingConfirms.put(channel.getNextPublishSeqNo(), String.valueOf(i));
            channel.basicPublish("", QUEUE_NAME, null, String.valueOf(i).getBytes());
        }
    }
}
