package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/6/6 下午7:23
 * @desc
 **/
public enum LogBusiEnum {
    充值("充值", "充值"),
    提现("提现", "提现"),
    消费("消费", "购买商品"),
    退款("退款", "退款或退货后退款"),
    赠送("赠送", "购买商品时，赠送积分"),
    兑换("兑换", "积分兑换商品");

    private String value;

    private String desc;

    LogBusiEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
