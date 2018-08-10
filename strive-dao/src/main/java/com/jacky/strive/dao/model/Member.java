package com.jacky.strive.dao.model;

import java.util.Date;

public class Member {
    private Integer memberId;

    private String memberNo;

    private String memberName;

    private Integer memberType;

    private String memberImage;

    private String memberPassword;

    private String memberMobile;

    private String memberEmail;

    private Integer memberQuotas;

    private Integer memberPoints;

    private Boolean memberStatus;

    private String recommendMemberNo;

    private Date createdAt;

    private Date updatedAt;

    private MemberAddress memberAddress;
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

    public Integer getMemberQuotas() {
        return memberQuotas;
    }

    public void setMemberQuotas(Integer memberQuotas) {
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


    public MemberAddress getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(MemberAddress memberAddress) {
        this.memberAddress = memberAddress;
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
}