package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/6/6 下午7:23
 * @desc
 **/
public enum ChargeStatusEnum {

    STATUS_NEW(0, "处理中"),
    STATUS_PROCESS(1, "处理中"),
    STATUS_SUCCESS(2, "成功"),
    STATUS_FAIL(3, "失败");

    private Integer value;

    private String desc;

    ChargeStatusEnum(Integer value, String desc) {
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
