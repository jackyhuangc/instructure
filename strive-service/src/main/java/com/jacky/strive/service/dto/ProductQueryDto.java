package com.jacky.strive.service.dto;

import lombok.Data;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:58
 * @desc
 **/
@Data
public class ProductQueryDto extends PageDto {

    private String productNo;

    private String productName;

    private String productAttr;
}
