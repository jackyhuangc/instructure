package com.jacky.common.util;

import org.apache.http.util.EncodingUtils;

import java.util.Base64;

/**
 * Base64编码工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
public class Base64Util {

    public static String base64Encode(String strObj) {

        byte[] bytes = EncodingUtils.getBytes(strObj, "utf-8");
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String base64Decode(String strObj) {
        byte[] bytes = Base64.getDecoder().decode(strObj);

        return EncodingUtils.getString(bytes, "utf-8");
    }
}
