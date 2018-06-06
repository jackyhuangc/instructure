package com.jacky.strive.service.dto;

import lombok.Data;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:59
 * @desc
 **/
@Data
public class MemberLoginDto {

    private String memberNo;

    private String memberMobile;

    private String password;
}
