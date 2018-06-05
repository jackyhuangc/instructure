package com.jacky.strive.dao.model;

import java.util.Date;

public class KeyValue {
    private Integer keyvalueId;

    private String keyvalueKey;

    private String keyvalueValue;

    private String keyvalueMemo;

    private Date createAt;

    private Date updateAt;

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

    public String getKeyvalueValue() {
        return keyvalueValue;
    }

    public void setKeyvalueValue(String keyvalueValue) {
        this.keyvalueValue = keyvalueValue == null ? null : keyvalueValue.trim();
    }

    public String getKeyvalueMemo() {
        return keyvalueMemo;
    }

    public void setKeyvalueMemo(String keyvalueMemo) {
        this.keyvalueMemo = keyvalueMemo == null ? null : keyvalueMemo.trim();
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}