package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

public class MemberCharge {
    private Integer chargeId;

    private String chargeNo;

    private Integer chargeType;

    private String payType;

    private String memberNo;

    private String cardNum;

    private BigDecimal chargeQuotas;

    private Integer chargePoints;

    private Integer chargeStatus;

    private String chargeMemo;

    private String openId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeNo() {
        return chargeNo;
    }

    public void setChargeNo(String chargeNo) {
        this.chargeNo = chargeNo == null ? null : chargeNo.trim();
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public BigDecimal getChargeQuotas() {
        return chargeQuotas;
    }

    public void setChargeQuotas(BigDecimal chargeQuotas) {
        this.chargeQuotas = chargeQuotas;
    }

    public Integer getChargePoints() {
        return chargePoints;
    }

    public void setChargePoints(Integer chargePoints) {
        this.chargePoints = chargePoints;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeMemo() {
        return chargeMemo;
    }

    public void setChargeMemo(String chargeMemo) {
        this.chargeMemo = chargeMemo == null ? null : chargeMemo.trim();
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}