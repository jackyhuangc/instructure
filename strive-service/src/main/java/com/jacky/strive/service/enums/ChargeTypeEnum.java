package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/6/6 下午7:23
 * @desc
 **/
public enum ChargeTypeEnum {

    CHARGE(0, "充值"),
    WITHDRAW(1, "提现");

    private Integer value;

    private String desc;

    ChargeTypeEnum(Integer value, String desc) {
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
