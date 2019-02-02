package com.jacky.strive.dao.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class MemberAddress {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer addressId;

    private String memberNo;

    private String shippingUser;

    private String shippingMobile;

    private String shippingDistrict;

    private String shippingAddress;

    private Boolean shippingDefault;

    private Date createdAt;

    private Date updatedAt;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
    }

    public String getShippingUser() {
        return shippingUser;
    }

    public void setShippingUser(String shippingUser) {
        this.shippingUser = shippingUser == null ? null : shippingUser.trim();
    }

    public String getShippingMobile() {
        return shippingMobile;
    }

    public void setShippingMobile(String shippingMobile) {
        this.shippingMobile = shippingMobile == null ? null : shippingMobile.trim();
    }

    public String getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict == null ? null : shippingDistrict.trim();
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress == null ? null : shippingAddress.trim();
    }

    public Boolean getShippingDefault() {
        return shippingDefault;
    }

    public void setShippingDefault(Boolean shippingDefault) {
        this.shippingDefault = shippingDefault;
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