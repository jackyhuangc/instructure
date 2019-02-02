package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class MemberLog {
    private Integer logId;

    private Integer logType;

    private String logBusi;

    private String memberNo;

    private BigDecimal memberQuotas;

    private Integer memberPoints;

    private String productNo;

    private BigDecimal productAmount;

    private String logMemo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    private String payType;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogBusi() {
        return logBusi;
    }

    public void setLogBusi(String logBusi) {
        this.logBusi = logBusi == null ? null : logBusi.trim();
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public BigDecimal getMemberQuotas() {
        return memberQuotas;
    }

    public void setMemberQuotas(BigDecimal memberQuotas) {
        this.memberQuotas = memberQuotas;
    }

    public Integer getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(Integer memberPoints) {
        this.memberPoints = memberPoints;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo == null ? null : productNo.trim();
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public String getLogMemo() {
        return logMemo;
    }

    public void setLogMemo(String logMemo) {
        this.logMemo = logMemo == null ? null : logMemo.trim();
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
}