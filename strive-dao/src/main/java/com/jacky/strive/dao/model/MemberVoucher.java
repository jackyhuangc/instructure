package com.jacky.strive.dao.model;

import java.util.Date;

public class MemberVoucher {
    private Integer voucherId;

    private String memberNo;

    private Integer voucherQuotas;

    private Boolean voucherValid;

    private Date voucherBegin;

    private Date voucherEnd;

    private String orderNo;

    private String voucherMemo;

    private Date createdAt;

    private Date updatedAt;

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public Integer getVoucherQuotas() {
        return voucherQuotas;
    }

    public void setVoucherQuotas(Integer voucherQuotas) {
        this.voucherQuotas = voucherQuotas;
    }

    public Boolean getVoucherValid() {
        return voucherValid;
    }

    public void setVoucherValid(Boolean voucherValid) {
        this.voucherValid = voucherValid;
    }

    public Date getVoucherBegin() {
        return voucherBegin;
    }

    public void setVoucherBegin(Date voucherBegin) {
        this.voucherBegin = voucherBegin;
    }

    public Date getVoucherEnd() {
        return voucherEnd;
    }

    public void setVoucherEnd(Date voucherEnd) {
        this.voucherEnd = voucherEnd;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getVoucherMemo() {
        return voucherMemo;
    }

    public void setVoucherMemo(String voucherMemo) {
        this.voucherMemo = voucherMemo == null ? null : voucherMemo.trim();
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