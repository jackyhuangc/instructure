package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private Integer productId;

    private String productNo;

    private String productName;

    private String productAttr;

    private String productImage;

    private BigDecimal productPrice;

    private BigDecimal productAmount;

    private String productUnit;

    private BigDecimal productDiscountRate;

    private Integer productPoints;

    private Integer productDeliverFee;

    private Integer productExchange;

    private Integer productPromotion;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productPromotionBegin;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productPromotionEnd;

    private Boolean productStatus;

    private String productShop;

    private BigDecimal productOpinionRate;

    private String productImageList;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    private String productIntroduce;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo == null ? null : productNo.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr == null ? null : productAttr.trim();
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage == null ? null : productImage.trim();
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit == null ? null : productUnit.trim();
    }

    public BigDecimal getProductDiscountRate() {
        return productDiscountRate;
    }

    public void setProductDiscountRate(BigDecimal productDiscountRate) {
        this.productDiscountRate = productDiscountRate;
    }

    public Integer getProductPoints() {
        return productPoints;
    }

    public void setProductPoints(Integer productPoints) {
        this.productPoints = productPoints;
    }

    public Integer getProductDeliverFee() {
        return productDeliverFee;
    }

    public void setProductDeliverFee(Integer productDeliverFee) {
        this.productDeliverFee = productDeliverFee;
    }

    public Integer getProductExchange() {
        return productExchange;
    }

    public void setProductExchange(Integer productExchange) {
        this.productExchange = productExchange;
    }

    public Integer getProductPromotion() {
        return productPromotion;
    }

    public void setProductPromotion(Integer productPromotion) {
        this.productPromotion = productPromotion;
    }

    public Date getProductPromotionBegin() {
        return productPromotionBegin;
    }

    public void setProductPromotionBegin(Date productPromotionBegin) {
        this.productPromotionBegin = productPromotionBegin;
    }

    public Date getProductPromotionEnd() {
        return productPromotionEnd;
    }

    public void setProductPromotionEnd(Date productPromotionEnd) {
        this.productPromotionEnd = productPromotionEnd;
    }

    public Boolean getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Boolean productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductShop() {
        return productShop;
    }

    public void setProductShop(String productShop) {
        this.productShop = productShop == null ? null : productShop.trim();
    }

    public BigDecimal getProductOpinionRate() {
        return productOpinionRate;
    }

    public void setProductOpinionRate(BigDecimal productOpinionRate) {
        this.productOpinionRate = productOpinionRate;
    }

    public String getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(String productImageList) {
        this.productImageList = productImageList == null ? null : productImageList.trim();
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

    public String getProductIntroduce() {
        return productIntroduce;
    }

    public void setProductIntroduce(String productIntroduce) {
        this.productIntroduce = productIntroduce == null ? null : productIntroduce.trim();
    }
}