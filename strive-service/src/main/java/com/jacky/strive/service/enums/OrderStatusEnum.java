package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/5/10 下午6:03
 * @desc
 **/
public enum OrderStatusEnum {

    // 关键显示，进行中(0.2.4.7)/待发货(3)/待收货(6)/已完成(1.5.8.9)
    STATUS_NEW(0, "新订单，待支付(已下单)，进行中"),
    STATUS_CLOSED(1, "已关闭(完成订单)，已完成"),
    STATUS_PAID(2, "已支付(已支付-未确认退/兑/发)，进行中"),

    // FIXME 如果自动发货确认=false，才有以下3中状态，默认会变成待确认
    STATUS_CONFIRM(3, "待确认，可选择退款和发货，待发货/待退款"),// 该状态仅仅MM可用
    STATUS_REVOKING(4, "待退款(已申请退款)，进行中，可能需要审核"),
    STATUS_DELIVERING(5, "待发货(已申请发货)，进行中"),

    // 针对自动发货确认=false时，只能处理STATUS_DELIVERING订单，才能发货
    // 针对自动发货确认=true时，只能处理STATUS_PAID订单，才能发货
    STATUS_DELIVERED(6, "已发货，待收货"),
    STATUS_RECEIVED(7, "已收货(完成订单)，已完成"),

    STATUS_REFUNDING(8, "待退货(已申请退货)，进行中"),
    // 退货时，需要退款，同时完成STATUS_REVOKED操作，事务处理
    STATUS_REFUNDED(9, "已退货，待退款，可能需要审核"),
    // 针对自动发货确认=false时，只能处理STATUS_REVOKING订单，才能退款
    // 针对自动发货确认=true时，只能处理STATUS_PAID订单，才能退款
    STATUS_REVOKED(10, "已退款(完成订单)，已完成");

    private Integer value;

    private String desc;

    OrderStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
