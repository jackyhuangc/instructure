/*
 * 包命名规则
 * com.公司名.项目名.模块名...
 * Java的包名都有小写单词组成，类名首字母大写；包的路径符合所开发的 系统模块的 定义
 * 公司名可用域名代替，可避免重名
 */
package com.jacky.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加解密工具类
 *
 * @author Jacky
 * @version 2017.1128
 * @since 1.8
 */
public class Md5Util {

    /**
     * 全局数组 常量命名规则-所有字母大写，如果有多个单词组成，单词与单词之间以” _“隔开。而
     * 且该变量必须是公共、静态、final类型
     */
    public final static String[] STR_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * 测试常量字段，值为{@value}
     */
    public final static String STR_TEST = "测试";

    /**
     * 返回形式为数字跟字符串，方法命名规则-首字母必须小写，如果该变量名有多个单词组成，后面的单词首字母 大写
     *
     * @param byeDigest md5签名摘要信息字节数组
     * @return 摘要字符串
     */
    private static String byteToArrayString(byte byeDigest) {
        /*
         *
         * 变量类型和首字母对照关系如下表： 数据类型/对象类型 / 变量前缀 / 备注 byte bye char chr float flt
         * boolean bln 做布尔变量时，使用bln Integer/int int String str Single sng short
         * sht Long/long lng Double/double dbl Currency cur Variant bln astr obj
         * vnt 做布尔变量用时，用bln，做字符串数组用时，用astr，做为对象使用时，用obj，不确定时，用vnt。
         * 对于数组，在数据类型的前缀前再增加一个a，例如字符串数组为astr。对于在多个函数内都要使用的全局变量，在前面再增加“g_”。
         * 例如一个全局的字符串变量：g_strUserInfo。
         *
         */

        int intRet = byeDigest;
        if (intRet < 0) {
            intRet += 256;
        }
        int iD1 = intRet / 16;
        int iD2 = intRet % 16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte 字节数组
     * @return 16进制字符串
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 字符串md5加密方法
     *
     * @param strObj 需加密的字符串
     * @return 加密后的字符串
     */
    public static String encode(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}