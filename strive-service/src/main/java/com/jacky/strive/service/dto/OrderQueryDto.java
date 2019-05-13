package com.jacky.strive.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:58
 * @desc
 **/
@Data
public class OrderQueryDto extends PageDto {

    private String orderNo;

    private String memberNo;

    private String productNo;

    private String productName;

    private Integer orderType;

    private String orderStatus;

    private boolean includeImage=false;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
