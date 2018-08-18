package com.jacky.strive.dao.model;

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

    private Integer productAmount;

    private String logMemo;

    private Date createdAt;

    private Date updatedAt;

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

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
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
}