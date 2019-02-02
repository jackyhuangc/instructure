package com.jacky.strive.common;

import org.apache.http.util.EncodingUtils;

import java.util.Base64;

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
