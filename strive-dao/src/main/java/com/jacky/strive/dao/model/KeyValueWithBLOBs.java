package com.jacky.strive.dao.model;

public class KeyValueWithBLOBs extends KeyValue {
    private String keyvalueValue;

    private String keyvalueMemo;

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
}