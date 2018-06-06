package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/6/6 下午7:23
 * @desc
 **/
public enum LogBusiEnum {

    IN(0, "充值(赠送)"),
    OUT(1, "提现(兑换)");

    private Integer value;

    private String desc;

    LogBusiEnum(Integer value, String desc) {
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
