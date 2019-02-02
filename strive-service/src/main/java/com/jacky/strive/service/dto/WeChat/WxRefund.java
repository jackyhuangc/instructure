package com.jacky.strive.service.dto.WeChat;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class WxRefund {

    @NotNull(message = "请输入订单号")
    @NotEmpty(message = "请输入订单号")
    String out_trade_no; // 订单号


    @NotNull(message = "请输入支付方式1是微信3是支付宝")
    @Min(value = 1,message = "请输入支付类型:是微信3是支付宝")
    Integer payWay;//支付方式1微信3支付宝

    String totalFee;//总金额
    String refundFee;//退款金额
}