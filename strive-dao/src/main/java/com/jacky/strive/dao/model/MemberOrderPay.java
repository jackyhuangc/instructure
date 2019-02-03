package com.jacky.strive.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class MemberOrderPay {
    private Integer payId;

    private String payBatchNo;

    private String payType;

    private String memberNo;

    private BigDecimal payQuotas;

    private Integer payPoints;

    private Integer payStatus;

    private String payMemo;

    private String openId;

    private Date createdAt;

    private Date updatedAt;

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getPayBatchNo() {
        return payBatchNo;
    }

    public void setPayBatchNo(String payBatchNo) {
        this.payBatchNo = payBatchNo == null ? null : payBatchNo.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public BigDecimal getPayQuotas() {
        return payQuotas;
    }

    public void setPayQuotas(BigDecimal payQuotas) {
        this.payQuotas = payQuotas;
    }

    public Integer getPayPoints() {
        return payPoints;
    }

    public void setPayPoints(Integer payPoints) {
        this.payPoints = payPoints;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayMemo() {
        return payMemo;
    }

    public void setPayMemo(String payMemo) {
        this.payMemo = payMemo == null ? null : payMemo.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
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