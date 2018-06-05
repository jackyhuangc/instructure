package com.jacky.strive.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private Integer productId;

    private Integer productNo;

    private String productType;

    private String productName;

    private String productImage;

    private Integer productPrice;

    private BigDecimal productDiscountRate;

    private Integer productPoints;

    private Integer productDeliverFee;

    private Integer productExchange;

    private Integer productPromotion;

    private Date productPromotionBegin;

    private Date productPromotionEnd;

    private String productStatus;

    private String productShop;

    private BigDecimal productOpinionRate;

    private Date createdAt;

    private Date updatedAt;

    private String productIntroduce;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage == null ? null : productImage.trim();
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
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

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus == null ? null : productStatus.trim();
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