package com.jacky.strive.service.dto;

import lombok.Data;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:58
 * @desc
 **/
@Data
public class UserQueryDto extends PageDto {

    private String userId;

    private String userName;

    private String telphone;
}
