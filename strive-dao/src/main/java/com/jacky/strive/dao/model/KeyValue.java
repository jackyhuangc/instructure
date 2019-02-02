package com.jacky.strive.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class KeyValue {
    private Integer keyvalueId;

    private String keyvalueKey;

    private String keyvalueMemo;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Date updatedAt;

    private String keyvalueValue;

    public Integer getKeyvalueId() {
        return keyvalueId;
    }

    public void setKeyvalueId(Integer keyvalueId) {
        this.keyvalueId = keyvalueId;
    }

    public String getKeyvalueKey() {
        return keyvalueKey;
    }

    public void setKeyvalueKey(String keyvalueKey) {
        this.keyvalueKey = keyvalueKey == null ? null : keyvalueKey.trim();
    }

    public String getKeyvalueMemo() {
        return keyvalueMemo;
    }

    public void setKeyvalueMemo(String keyvalueMemo) {
        this.keyvalueMemo = keyvalueMemo == null ? null : keyvalueMemo.trim();
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

    public String getKeyvalueValue() {
        return keyvalueValue;
    }

    public void setKeyvalueValue(String keyvalueValue) {
        this.keyvalueValue = keyvalueValue == null ? null : keyvalueValue.trim();
    }
}