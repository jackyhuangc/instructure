package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class MemberOrderInterest {
    private Integer interestId;

    private String orderNo;

    private String productNo;

    private String memberNo;

    private BigDecimal orderQuotas;

    private BigDecimal interestRate;

    private BigDecimal interestQuotas;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date interestAt;

    private String interestMemo;

    private Date createdAt;

    private Date updatedAt;

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo == null ? null : productNo.trim();
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public BigDecimal getOrderQuotas() {
        return orderQuotas;
    }

    public void setOrderQuotas(BigDecimal orderQuotas) {
        this.orderQuotas = orderQuotas;
    }

    public BigDecimal getInterestQuotas() {
        return interestQuotas;
    }

    public void setInterestQuotas(BigDecimal interestQuotas) {
        this.interestQuotas = interestQuotas;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }


    public Date getInterestAt() {
        return interestAt;
    }

    public void setInterestAt(Date interestAt) {
        this.interestAt = interestAt;
    }

    public String getInterestMemo() {
        return interestMemo;
    }

    public void setInterestMemo(String interestMemo) {
        this.interestMemo = interestMemo == null ? null : interestMemo.trim();
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