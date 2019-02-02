package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Member {
    private Integer memberId;

    private String memberNo;

    private String memberName;

    private Integer memberType;

    private String memberImage;

    private String memberSource;

    private String memberPassword;

    private String memberMobile;

    private String memberEmail;

    private BigDecimal memberQuotas;

    private Integer memberPoints;
    private BigDecimal memberDeposit;
    private BigDecimal memberWithdraw;
    private BigDecimal memberBuyAction;
    private BigDecimal memberSellAction;
    private BigDecimal memberInterestAction;

    private Boolean memberStatus;

    private String memberDistrict;

    private String memberAddress;

    private String memberRemark;

    private String recommendMemberNo;

    private String unionId;

    private String openId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    //private MemberAddress memberAddress;
    private MemberCharge memberCharge;
    private MemberLog memberLog;
    private MemberVoucher memberVoucher;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage == null ? null : memberImage.trim();
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword == null ? null : memberPassword.trim();
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile == null ? null : memberMobile.trim();
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail == null ? null : memberEmail.trim();
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

    public Boolean getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Boolean memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getRecommendMemberNo() {
        return recommendMemberNo;
    }

    public void setRecommendMemberNo(String recommendMemberNo) {
        this.recommendMemberNo = recommendMemberNo == null ? null : recommendMemberNo.trim();
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

    public MemberCharge getMemberCharge() {
        return memberCharge;
    }

    public void setMemberCharge(MemberCharge memberCharge) {
        this.memberCharge = memberCharge;
    }

    public MemberLog getMemberLog() {
        return memberLog;
    }

    public void setMemberLog(MemberLog memberLog) {
        this.memberLog = memberLog;
    }

    public MemberVoucher getMemberVoucher() {
        return memberVoucher;
    }

    public void setMemberVoucher(MemberVoucher memberVoucher) {
        this.memberVoucher = memberVoucher;
    }

    public String getMemberRemark() {
        return memberRemark;
    }

    public void setMemberRemark(String memberRemark) {
        this.memberRemark = memberRemark;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMemberSource() {
        return this.memberSource;
    }

    public void setMemberSource(String memberSource) {
        this.memberSource = memberSource;
    }

    public String getMemberDistrict() {
        return this.memberDistrict;
    }

    public void setMemberDistrict(String memberDistrict) {
        this.memberDistrict = memberDistrict;
    }

    public String getMemberAddress() {
        return this.memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }


    public BigDecimal getMemberDeposit() {
        return memberDeposit;
    }

    public void setMemberDeposit(BigDecimal memberDeposit) {
        this.memberDeposit = memberDeposit;
    }

    public BigDecimal getMemberWithdraw() {
        return memberWithdraw;
    }

    public void setMemberWithdraw(BigDecimal memberWithdraw) {
        this.memberWithdraw = memberWithdraw;
    }

    public BigDecimal getMemberBuyAction() {
        return memberBuyAction;
    }

    public void setMemberBuyAction(BigDecimal memberBuyAction) {
        this.memberBuyAction = memberBuyAction;
    }

    public BigDecimal getMemberSellAction() {
        return memberSellAction;
    }

    public void setMemberSellAction(BigDecimal memberSellAction) {
        this.memberSellAction = memberSellAction;
    }

    public BigDecimal getMemberInterestAction() {
        return memberInterestAction;
    }

    public void setMemberInterestAction(BigDecimal memberInterestAction) {
        this.memberInterestAction = memberInterestAction;
    }
}