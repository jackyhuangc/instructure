package com.jacky.strive.service.dto.WeChat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * WxPrepay
 */
@Data
public class WxPrepay {

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("prepay_id")
    private String prepayId;

    // 二维码链接，trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
    @JsonProperty("code_url")
    private String codeUrl;

    @JsonProperty("mch_id")
    private String appId;

    @JsonProperty("mch_id")
    private String mchId;

    @JsonProperty("nonce_str")
    private String nonceStr;

    @JsonProperty("paySign")
    private String paySign;

    @JsonProperty("timeStamp")
    private String timeStamp;
}
