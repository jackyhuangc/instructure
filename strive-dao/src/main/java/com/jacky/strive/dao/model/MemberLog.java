package com.jacky.strive.dao.model;

import java.util.Date;

public class MemberLog {
    private Integer logId;

    private String logType;

    private String logBusi;

    private String memberNo;

    private Integer memberQuotas;

    private Integer memberPoints;

    private String logMemo;

    private Date createdAt;

    private Date updatedAt;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public String getLogBusi() {
        return logBusi;
    }

    public void setLogBusi(String logBusi) {
        this.logBusi = logBusi == null ? null : logBusi.trim();
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo == null ? null : memberNo.trim();
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

    public String getLogMemo() {
        return logMemo;
    }

    public void setLogMemo(String logMemo) {
        this.logMemo = logMemo == null ? null : logMemo.trim();
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