package com.jacky.strive.service.dto.WeChat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * WxSession
 */
@Data
public class WxSession {
    @JsonProperty("openid")
    private String openId;

    @JsonProperty("session_key")
    private String sessionKey;

    @JsonProperty("unionid")
    private String unionId;

    @JsonProperty("errcode")
    private String errCode;

    @JsonProperty("errmsg")
    private String errMsg;
}
