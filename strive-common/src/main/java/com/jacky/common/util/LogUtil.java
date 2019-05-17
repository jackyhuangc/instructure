package com.jacky.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String message) {

        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void warn(String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public static void error(String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public static void error(Exception ex) {
        if (logger.isErrorEnabled()) {
            logger.error(ex.getMessage());
        }
    }

    public static void error(String message, Throwable ex) {
        if (logger.isErrorEnabled()) {
            logger.error(message, ex);
        }
    }
}
