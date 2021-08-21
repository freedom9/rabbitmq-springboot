package com.freedom.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("114.55.253.140");
        factory.setUsername("freedom");
        factory.setPassword("freedom");

         Connection connection = factory.newConnection();
         return connection.createChannel();
    }
}
