package com.jacky.strive.common;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends org.springframework.util.StringUtils {

    public static final String EMPTY = "";


    /**
     * 判断两个支付串是否相等，null=''="   " ,忽略大小写.
     *
     * @param str1
     * @param str2
     *
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        str1 = str1 == null ? "" : str1.trim();
        str2 = str2 == null ? "" : str2.trim();
        return str1.compareToIgnoreCase(str2) == 0;
    }

    public static boolean isEmtpy(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断一个值是否是真，下面的情况为真：1，yes,true,y,否则为false.
     *
     * @param str
     *
     * @return
     */
    public static boolean checkBoolean(String str) {
        if (str == null) {
            return false;
        }
        str = str.toUpperCase().trim();
        switch (str) {
            case "YES":
            case "TRUE":
            case "Y":
            case "1":
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断手机号码
     */

    public static boolean isMobileNumber(String mobileNumber) {
        return !StringUtil.isEmtpy(formatMobilePhone(mobileNumber));
    }

    /**
     * 判断手机号码
     */

    public static boolean isMobileNumber(String mobileNumber, boolean needReplace) {
        return !StringUtil.isEmtpy(formatMobilePhone(mobileNumber, needReplace));
    }

    /***
     * 手机号码转换
     * */
    public static String formatPhone(String relativeTel) {
        String tel = formatMobilePhone(relativeTel);
        if (!isEmtpy(tel)) {
            return tel;
        }
        return relativeTel;
    }

    /***
     * 手机号码转换
     * */
    public static String formatMobilePhone(String relativeTel) {

        return formatMobilePhone(relativeTel, true);
    }

    /***
     * 手机号码转换
     * */
    public static String formatMobilePhone(String relativeTel, boolean needReplace) {
        if (isEmtpy(relativeTel)) {
            return "";
        }
        if (needReplace) {
            relativeTel = relativeTel.replaceAll("[^0-9]", "");
        }
        Pattern p = Pattern.compile("1[34578]\\d{9}$");
        Matcher m = p.matcher(relativeTel);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * 判断是否数字
     */
    public static boolean isNumeric(String text) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher isNum = pattern.matcher(text);
        return isNum.matches();
    }

    /**
     * 简单判断是否包含4个字节的Unicode
     */

    public static boolean containMb4(String text) {
        int len = text.length();
        return len != text.codePointCount(0, len);
    }

    /**
     * 简单判断是否包含字母
     */

    public static boolean containLetter(String text) {
        Pattern p = Pattern.compile("/[a-zA-Z]/");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static String getUniqueString(String placeholder) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter md = DateTimeFormatter.ofPattern("MMdd");
        Integer hash = getApHash(UUID.randomUUID().toString().replace("-", ""));

        String number = String.format("%s%s%s%s", (now.getYear() - 2016), now.format(md),
                StringUtils.substring(String.valueOf(System.currentTimeMillis()), 10),
                hash.toString().length() > 10 ? StringUtils.substring(hash.toString(), 10) :
                        String.format("%010d", hash));
        if (StringUtils.isEmpty(placeholder)) {
            return number;
        }
        return placeholder + number;
    }

    public static Integer getApHash(String value) {
        int hash, i;
        for (hash = value.length(), i = 0; i < value.length(); ++i) {
            if ((i & 1) == 0) {
                hash ^= ((hash << 7) ^ value.charAt(i) ^ (hash >> 3));
            } else {
                hash ^= (~((hash << 11) ^ value.charAt(i) ^ (hash >> 5)));
            }
        }
        return (hash & 0x7FFFFFFF);
    }

    public static String format(double value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }

    public static String format(double value) {
        return format(value, "0.00");
    }

    /**
     * 字符串格式化
     *
     * @param format
     *         原来的字符串
     * @param args
     *         参数
     *
     * @return 格式化的字符串（格式化错误，返回原来的值）
     */
    public static String format(String format, Object... args) {
        String to;

        try {
            to = String.format(format, args);
        } catch (Exception e) {
            to = format;
        }

        return to;
    }

    /**
     * 子字符串方法
     *
     * @param from
     *         原始字符串
     * @param len
     *         需要的长度
     * @param append
     *         添加字符串
     *
     * @return 子字符串
     */
    public static String start(String from, int len, char append) {
        String key = "";
        if (null == from) {
            return key;
        }

        if (from.length() < len) {
            StringBuilder sb = new StringBuilder();
            sb.append(from);
            for (int i = 0; i < len - from.length(); ++i) {
                sb.append(append);
            }
            key = sb.toString();
        } else {
            key = from.substring(0, len);
        }

        return key;
    }

    /**
     * 子字符串方法
     *
     * @param from
     *         原始字符串
     * @param len
     *         需要的长度
     * @param append
     *         添加字符串
     *
     * @return 子字符串
     */
    public static String start(String from, int len, String append) {
        String ret;

        int strLen = from.length();
        if (strLen <= len) {
            ret = from.substring(0, strLen);
        } else {
            ret = from.substring(0, len) + append;
        }

        return ret;
    }

    /**
     * 子字符串方法
     *
     * @param from
     *         原始字符串
     * @param len
     *         需要的长度
     *
     * @return 子字符串
     */
    public static String start(String from, int len) {
        return start(from, len, "...");
    }

    /**
     * 取字符串后位
     *
     * @param from
     *         原始字符串
     * @param len
     *         长度
     *
     * @return 子字符串
     */
    public static String end(String from, int len) {
        String ret = from;

        int strLen = from.length();
        if (len > strLen) {
            len = strLen;
        }
        ret = from.substring(strLen - len, strLen - 1);

        return ret;
    }

    public static Date parseBirthdayFromIdCard(String idcard) {
        if (isEmtpy(idcard)) {
            return null;
        }
        idcard = idcard.trim();
        String birthdayStr;
        if (idcard.length() == 18) {
            birthdayStr = idcard.substring(6, 14);
        } else if (idcard.length() == 15) {
            birthdayStr = "19" + idcard.substring(6, 12);
        } else {
            return null;
        }

        try {
            return DateUtil.parseDate(birthdayStr, "yyyyMMdd");
        } catch (Exception ex) {
            LogUtil.error(ex);
        }
        return null;
    }


    /***
     * 判断字符串是否全是中文
     * */
    public static boolean isAllChinese(String str) {
        Pattern p = Pattern.compile("[^\u4e00-\u9fa5]");
        return !p.matcher(str).find();
    }

    public static String toStr(String str) {
        if (str == null) {
            return EMPTY;
        }
        return str;
    }

    /**
     * 正则替换所有特殊字符
     *
     * @param str
     *
     * @return
     */
    public static String replaceSpecStr(String str) {
        if (null != str && !"".equals(str.trim())) {
            String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }

    /**
     * 验证邮箱
     *
     * @param email
     *
     * @return
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * unicode转中文
     *
     * @param str
     *
     * @return
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /***
     * 首字母大写
     * @param string
     * @return
     */
    public static String toUpperFristChar(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }

    /**
     *  追加URL参数
     * @param url
     * @param paramName
     * @param paramVal
     * @return
     */
    public static String appendUrlParam(String url, String paramName, String paramVal) {
        StringBuffer urlBuffer = new StringBuffer(url);
        if (url.contains("?")) {
            urlBuffer.append("&" + paramName + "=");
            urlBuffer.append(paramVal);
        }else{
            urlBuffer.append("?" + paramName + "=");
            urlBuffer.append(paramVal);
        }
        return urlBuffer.toString();
    }

    /**
     * 验证字符串是否是有效的网址
     *
     * @param url string
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

}
