package com.jacky.strive.service.dto;

import com.jacky.strive.service.dto.WeChat.WxUserInfo;
import lombok.Data;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:58
 * @desc
 **/
@Data
public class LoginThirdDto extends PageDto {

    private String loginType;

    private String code;

    private String encryptedData;

    private String iv;

    private WxUserInfo userInfo;
}