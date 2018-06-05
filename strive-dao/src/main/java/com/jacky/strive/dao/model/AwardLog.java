package com.jacky.strive.dao.model;

import java.util.Date;

public class AwardLog {
    private Integer logId;

    private String awardPeriod;

    private Integer awardResult;

    private Date awardBegin;

    private Date awardEnd;

    private Date createdAt;

    private Date updatedAt;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getAwardPeriod() {
        return awardPeriod;
    }

    public void setAwardPeriod(String awardPeriod) {
        this.awardPeriod = awardPeriod == null ? null : awardPeriod.trim();
    }

    public Integer getAwardResult() {
        return awardResult;
    }

    public void setAwardResult(Integer awardResult) {
        this.awardResult = awardResult;
    }

    public Date getAwardBegin() {
        return awardBegin;
    }

    public void setAwardBegin(Date awardBegin) {
        this.awardBegin = awardBegin;
    }

    public Date getAwardEnd() {
        return awardEnd;
    }

    public void setAwardEnd(Date awardEnd) {
        this.awardEnd = awardEnd;
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