package com.jacky.strive.service.enums;

/**
 * @author huangchao
 * @create 2018/5/10 下午6:03
 * @desc
 **/
public enum ProductPromotionEnum {

    NONE(0, "不促销"),
    DISCOUNT(1, "折扣"),
    TIMELIMIT(2, "限时");

    private Integer value;

    private String desc;

    ProductPromotionEnum(Integer value, String desc) {
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
