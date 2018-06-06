package com.jacky.strive.dao.model;

import java.util.Date;

public class MemberOrder {
    private Integer orderId;

    private String orderNo;

    private Integer orderType;

    private String memberNo;

    private String orderProductNo;

    private String orderProductName;

    private String orderGiftNo;

    private String orderGiftName;

    private Integer orderPrice;

    private Integer orderAmount;

    private Integer orderDiscount;

    private Integer orderVoucher;

    private Integer orderDeliverFee;

    private Integer orderPoints;

    private Integer orderStatus;

    private String orderAwardPeriod;

    private String orderAwardResult;

    private String shippingUser;

    private String shippingMobile;

    private String shippingDistrict;

    private String shippingAddress;

    private Date deliverAt;

    private String deliverMemo;

    private Date finishAt;

    private String finishMemo;

    private Date createdAt;

    private Date updatedAt;

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

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(Integer orderDiscount) {
        this.orderDiscount = orderDiscount;
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
}