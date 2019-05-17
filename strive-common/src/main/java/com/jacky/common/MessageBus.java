package com.jacky.common;

import com.jacky.common.enums.MessageEnum;
import com.jacky.common.util.LogUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.*;
import java.util.concurrent.TimeoutException;

/**
 * 消息总线工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc 支持多种推送形式
 **/
public class MessageBus {

    public static final String RABBIT_MQ_HOST = "127.0.0.1";
    public static final String RABBIT_MQ_USER_NAME = "admin";
    public static final String RABBIT_MQ_PASSWORD_NAME = "123456";
    public static final String RABBIT_MQ_QUEUE_NAME = "rabbitMQ.test";
    public static final Boolean RABBIT_MQ_QUEUE_DURABLE = true;
    public static final com.rabbitmq.client.AMQP.BasicProperties RABBIT_MQ_MESSAGE_PERSISTENT = MessageProperties.PERSISTENT_TEXT_PLAIN;

    /**
     * 消息推送
     *
     * @param messageEnum
     * @param message
     */
    public static void push(MessageEnum messageEnum, String message) {

        try {
            switch (messageEnum) {
                case RABBIT_MQ:
                    pushToRabbitMQ(message);
                    break;
                case KAFKA:
                    break;
                case DATABASE:
                    break;
                default:
                    break;
            }

            LogUtil.info(String.format("push:%s,%s", messageEnum, message));
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 以RabbitMQ方式推送
     *
     * @param message
     * @throws IOException
     * @throws TimeoutException
     */
    private static void pushToRabbitMQ(String message) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //设置RabbitMQ相关信息
        factory.setHost(RABBIT_MQ_HOST);

        factory.setConnectionTimeout(30000);
        factory.setUsername(RABBIT_MQ_USER_NAME);
        factory.setPassword(RABBIT_MQ_PASSWORD_NAME);
        factory.setPort(5672);

        //创建一个新的连接
        Connection connection = factory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();
        // 声明一个队列
        // 队列持久化 我们需要确认RabbitMQ永远不会丢失我们的队列。为了这样，我们需要声明它为持久化
        channel.queueDeclare(RABBIT_MQ_QUEUE_NAME, RABBIT_MQ_QUEUE_DURABLE, false, false, null);

        channel.basicPublish("", RABBIT_MQ_QUEUE_NAME, RABBIT_MQ_MESSAGE_PERSISTENT, message.getBytes("UTF-8"));

        // 关闭通道和连接
        channel.close();
        connection.close();
    }
}
