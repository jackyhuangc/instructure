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
    STATUS_CONFIRM(3, "待确认，可选择退款和发货，待发货/待退款"),// 该状态仅仅MM可用
    STATUS_REVOKING(4, "待退款(已申请退款)，进行中"),
    STATUS_REVOKED(5, "已退款(完成订单)，已完成"),
    STATUS_DELIVERING(6, "已发货，待收货"),// 仅仅针对STATUS_CONFIRM订单，才能发货
    STATUS_REFUNDING(7, "待退货(已申请退货)，进行中"),
    STATUS_REFUNDED(8, "已退货，已完成"),
    STATUS_RECEIVED(9, "已收货(完成订单)，已完成");

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
