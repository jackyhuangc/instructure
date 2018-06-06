package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/5/10 下午6:03
 * @desc
 **/
public enum MemberTypeEnum {

    QUOTAS(0, "普通"),
    POINTS(1, "VIP");

    private Integer value;

    private String desc;

    MemberTypeEnum(Integer value, String desc) {
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
