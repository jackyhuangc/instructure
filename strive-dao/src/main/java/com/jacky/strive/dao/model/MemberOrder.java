package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class MemberOrder {
    private Integer orderId;

    private String orderNo;

    private String orderFrom;

    private Integer orderType;

    private String payType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payAt;

    private String payBatchNo;

    private BigDecimal payQuotas;

    private String memberNo;

    private String orderProductNo;

    private String orderProductName;

    private String orderProductModel;

    private String orderGiftNo;

    private String orderGiftName;

    private BigDecimal orderPrice;

    private BigDecimal orderAmount;

    private BigDecimal orderDiscount;

    private String orderVoucherNo;

    private Integer orderVoucher;

    private Integer orderDeliverFee;

    private Integer orderPoints;

    private Integer orderStatus;

    private BigDecimal orderOpinionRate;

    private String orderAwardPeriod;

    private String orderAwardResult;

    private BigDecimal orderAwardQuotas;

    private String shippingUser;

    private String shippingMobile;

    private String shippingDistrict;

    private String shippingAddress;

    private Date deliverAt;

    private String deliverMemo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishAt;

    private String finishMemo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    private String openId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public String getOrderProductNo() {
        return orderProductNo;
    }

    public void setOrderProductNo(String orderProductNo) {
        this.orderProductNo = orderProductNo == null ? null : orderProductNo.trim();
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName == null ? null : orderProductName.trim();
    }

    public String getOrderGiftNo() {
        return orderGiftNo;
    }

    public void setOrderGiftNo(String orderGiftNo) {
        this.orderGiftNo = orderGiftNo == null ? null : orderGiftNo.trim();
    }

    public String getOrderGiftName() {
        return orderGiftName;
    }

    public void setOrderGiftName(String orderGiftName) {
        this.orderGiftName = orderGiftName == null ? null : orderGiftName.trim();
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(BigDecimal orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getOrderVoucherNo() {
        return orderVoucherNo;
    }

    public void setOrderVoucherNo(String orderVoucherNo) {
        this.orderVoucherNo = orderVoucherNo == null ? null : orderVoucherNo.trim();
    }

    public Integer getOrderVoucher() {
        return orderVoucher;
    }

    public void setOrderVoucher(Integer orderVoucher) {
        this.orderVoucher = orderVoucher;
    }

    public Integer getOrderDeliverFee() {
        return orderDeliverFee;
    }

    public void setOrderDeliverFee(Integer orderDeliverFee) {
        this.orderDeliverFee = orderDeliverFee;
    }

    public Integer getOrderPoints() {
        return orderPoints;
    }

    public void setOrderPoints(Integer orderPoints) {
        this.orderPoints = orderPoints;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAwardPeriod() {
        return orderAwardPeriod;
    }

    public void setOrderAwardPeriod(String orderAwardPeriod) {
        this.orderAwardPeriod = orderAwardPeriod == null ? null : orderAwardPeriod.trim();
    }

    public String getOrderAwardResult() {
        return orderAwardResult;
    }

    public void setOrderAwardResult(String orderAwardResult) {
        this.orderAwardResult = orderAwardResult == null ? null : orderAwardResult.trim();
    }

    public String getShippingUser() {
        return shippingUser;
    }

    public void setShippingUser(String shippingUser) {
        this.shippingUser = shippingUser == null ? null : shippingUser.trim();
    }

    public String getShippingMobile() {
        return shippingMobile;
    }

    public void setShippingMobile(String shippingMobile) {
        this.shippingMobile = shippingMobile == null ? null : shippingMobile.trim();
    }

    public String getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict == null ? null : shippingDistrict.trim();
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress == null ? null : shippingAddress.trim();
    }

    public Date getDeliverAt() {
        return deliverAt;
    }

    public void setDeliverAt(Date deliverAt) {
        this.deliverAt = deliverAt;
    }

    public String getDeliverMemo() {
        return deliverMemo;
    }

    public void setDeliverMemo(String deliverMemo) {
        this.deliverMemo = deliverMemo == null ? null : deliverMemo.trim();
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    public String getFinishMemo() {
        return finishMemo;
    }

    public void setFinishMemo(String finishMemo) {
        this.finishMemo = finishMemo == null ? null : finishMemo.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayBatchNo() {
        return payBatchNo;
    }

    public void setPayBatchNo(String payBatchNo) {
        this.payBatchNo = payBatchNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }

    public String getOrderProductModel() {
        return orderProductModel;
    }

    public void setOrderProductModel(String orderProductModel) {
        this.orderProductModel = orderProductModel;
    }

    public BigDecimal getPayQuotas() {
        return payQuotas;
    }

    public void setPayQuotas(BigDecimal payQuotas) {
        this.payQuotas = payQuotas;
    }

    public BigDecimal getOrderOpinionRate() {
        return orderOpinionRate;
    }

    public void setOrderOpinionRate(BigDecimal orderOpinionRate) {
        this.orderOpinionRate = orderOpinionRate;
    }

    public BigDecimal getOrderAwardQuotas() {
        return orderAwardQuotas;
    }

    public void setOrderAwardQuotas(BigDecimal orderAwardQuotas) {
        this.orderAwardQuotas = orderAwardQuotas;
    }
}