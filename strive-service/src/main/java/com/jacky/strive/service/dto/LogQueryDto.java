package com.jacky.strive.service.dto;

import lombok.Data;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:58
 * @desc
 **/
@Data
public class LogQueryDto extends PageDto {

    private String memberNo;

    private Integer logType;

    private String logBusi;
}
