package com.jacky.strive.dao.model;

import java.util.Date;

public class MemberCard {
    private Integer cardId;

    private String memberNo;

    private String cardNum;

    private String cardUser;

    private String cardIdentity;

    private String cardMobile;

    private String cardBank;

    private Boolean cardStatus;

    private String cardInfo;

    private Boolean cardDefault;

    private Date createdAt;

    private Date updatedAt;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum == null ? null : cardNum.trim();
    }

    public String getCardUser() {
        return cardUser;
    }

    public void setCardUser(String cardUser) {
        this.cardUser = cardUser == null ? null : cardUser.trim();
    }

    public String getCardIdentity() {
        return cardIdentity;
    }

    public void setCardIdentity(String cardIdentity) {
        this.cardIdentity = cardIdentity == null ? null : cardIdentity.trim();
    }

    public String getCardMobile() {
        return cardMobile;
    }

    public void setCardMobile(String cardMobile) {
        this.cardMobile = cardMobile == null ? null : cardMobile.trim();
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank == null ? null : cardBank.trim();
    }

    public Boolean getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Boolean cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo == null ? null : cardInfo.trim();
    }

    public Boolean getCardDefault() {
        return cardDefault;
    }

    public void setCardDefault(Boolean cardDefault) {
        this.cardDefault = cardDefault;
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