package com.jacky.strive.common;

import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 断言工具类
 */
public final class AssertUtil extends Assert {
    
    private AssertUtil() {
    }
    
    /**
     * 断言value>targe
     * @param value value
     * @param target targe
     * @param message 如果失败，需要显示的消息。
     */
    public static void moreThan(Integer value, Integer target, String message) {
        notNull(value, message);
        notNull(target, message);
        isTrue(value.compareTo(target) > 0, message);
    }
    
    /**
     * 断言value>targe
     * @param value value
     * @param target targe
     * @param message 如果失败，需要显示的消息。
     */
    public static void moreThan(Long value, Long target, String message) {
        notNull(value, message);
        notNull(target, message);
        isTrue(value.compareTo(target) > 0, message);
    }
    
    /**
     * 断言value>targe
     * @param value value
     * @param target targe
     * @param message 如果失败，需要显示的消息。
     */
    public static void moreThan(BigDecimal value, BigDecimal target, String message) {
        notNull(value, message);
        notNull(target, message);
        isTrue(value.compareTo(target) > 0, message);
    }
}
