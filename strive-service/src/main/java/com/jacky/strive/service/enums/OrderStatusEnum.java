package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/5/10 下午6:03
 * @desc
 **/
public enum OrderStatusEnum {

    // 正常电商应有状态：0,1,2,4,6,7,8,9,10,11.12
    // 分享电商应有状态：3.4.5.6.7.8.9.10.11.12
    // 进销存类订单    6,9  出库，入库
    // 搬家服务类订单  0,1,2,11  下单，取消，支付，退款

    // 关键显示，进行中(待支付或待确认)(0.3)/待发货(2.5)/待收货(6)/已完成(1.7)/退货(退款)(4.8.9.10.11.12)
    STATUS_NEW(0, "新订单，待支付(已下单)，订单进行中"),
    STATUS_CLOSED(1, "已关闭，订单取消"),

    STATUS_PAID(2, "已支付(已支付-未确认退/兑/发)，订单进行中"),

    // FIXME 如果自动发货确认=false，才有以下3中状态，默认会变成待确认
    STATUS_CONFIRM(3, "待确认(请选择退款和发货)，订单进行中"),
    STATUS_REVOKING(4, "待退款(已申请退款)，订单进行中"),
    STATUS_DELIVERING(5, "待发货(已申请发货)，订单进行中"),

    // 针对自动发货确认=false时，只能处理STATUS_DELIVERING订单，才能发货
    // 针对自动发货确认=true时，只能处理STATUS_PAID订单，才能发货
    STATUS_DELIVERED(6, "待收货(已发货)，订单进行中"),
    STATUS_RECEIVED(7, "已收货，订单完成"),

    STATUS_REFUNDING(8, "待退货(已申请退货)，订单进行中"),
    // 退货时，需要退款，同时完成STATUS_REVOKED操作，事务处理
    STATUS_REFUNDED(9, "已退货，等待退款"),
    STATUS_REFUNDED_REJECT(10, "退货失败，订单完成"),

    // 针对自动发货确认=false时，自动处理STATUS_REVOKING订单，进行退款
    // 针对自动发货确认=true时，手动处理STATUS_REVOKING订单，才能退款
    STATUS_REVOKED(11, "已退款，订单完成"),
    STATUS_REVOKED_REJECT(12, "退款失败，订单完成");

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
