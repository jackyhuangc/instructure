package com.jacky.strive.common;

import qsq.biz.common.util.StringUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class SmsUtil {

    public static boolean sendSms(String mobile, String Content) {

        try {
            //短信宝短信API地址
            String httpUrl = "http://api.smsbao.com/sms";
            //在短信宝注册的用户名
            String testUsername = "tming775";
            //在短信宝注册的密码
            String testPassword = "tming520";

            StringBuffer httpArg = new StringBuffer();
            httpArg.append("u=").append(testUsername).append("&");
            httpArg.append("p=").append(md5(testPassword)).append("&");
            httpArg.append("m=").append(mobile).append("&");
            httpArg.append("c=").append(encodeUrlString(Content, "UTF-8"));

            String result = request(httpUrl, httpArg.toString());

            return !StringUtil.isEmtpy(result) && result.equals("0");
        } catch (Exception e) {
            LogUtil.error(e);
            return false;
        }
    }

    private static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    private static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }
}
