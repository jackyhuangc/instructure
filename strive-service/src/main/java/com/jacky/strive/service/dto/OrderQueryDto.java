package com.jacky.strive.service.dto;

import lombok.Data;

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

    private Integer orderStatus;
}
