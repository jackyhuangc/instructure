package com.jacky.common.enums;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc 消息格式枚举
 **/
public enum MessageEnum {

    /**
     * Rabbit_MQ消息
     */
    RABBIT_MQ(0),
    /**
     * Kafka消息
     */
    KAFKA(1),
    /**
     * 数据库消息
     */
    DATABASE(2);

    private int messageEnum;

    MessageEnum(int me) {
        messageEnum = me;
    }
}
