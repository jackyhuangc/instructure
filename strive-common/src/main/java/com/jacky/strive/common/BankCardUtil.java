package com.jacky.strive.common;

import lombok.Data;
import qsq.biz.common.util.JsonUtil;

public class BankCardUtil {

    public static boolean validate(String cardNo) {

        try {
            String verify = HttpUtil.get(String.format("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?cardBinCheck=true&cardNo=%s", cardNo), null);
            AliCardDto aliCardDto = JsonUtil.fromJson(verify, AliCardDto.class);
            return aliCardDto.isValidated();
        } catch (Exception e) {
            return false;
        }
    }

    @Data
    static class AliCardDto {
        /*
        * {
        "cardType": "DC",
        "bank": "ABC",
        "key": "6228431069522553671",
        "messages": [],
        "validated": true,
        "stat": "ok"
        }
        * */

        private String cardType;

        private String bank;

        private String key;

        private boolean validated;

        private String stat;
    }
}
