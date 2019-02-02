package com.jacky.strive.service.dto.WeChat;

import lombok.Data;

@Data
public class WxUserInfo {

    private String openId;

    private String nickName;

    private String gender;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String unionId;

    private String mobile;

    private String password;
}