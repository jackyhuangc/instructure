package com.jacky.strive.dao.model;

import java.util.Date;

public class KeyValue {
    private Integer keyvalueId;

    private String keyvalueKey;

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