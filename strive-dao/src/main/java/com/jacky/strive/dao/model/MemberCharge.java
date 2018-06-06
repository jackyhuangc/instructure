package com.jacky.strive.dao.model;

import java.util.Date;

public class MemberCharge {
    private Integer chargeId;

    private String chargeNo;

    private Integer chargeType;

    private String memberNo;

    private Integer chargeQuotas;

    private Integer chargePoints;

    private Integer chargeStatus;

    private String chargeMemo;

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

    public Integer getChargeQuotas() {
        return chargeQuotas;
    }

    public void setChargeQuotas(Integer chargeQuotas) {
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
}