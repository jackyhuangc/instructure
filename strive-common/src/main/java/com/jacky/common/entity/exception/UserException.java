package com.jacky.common.entity.exception;

import com.jacky.common.util.ExceptionUtil;

/**
 * 用户异常类
 *
 * @author Dyh
 */
public class UserException extends RuntimeException {
    
    private static final long serialVersionUID = -1152162357204508127L;
    
    /**
     * 异常构造函数
     *
     * @param message 消息
     */
    public UserException(String message) {
        super(message);
    }
    
    public UserException(String format, Object... args) {
        this(String.format(format, args));
    }
    
    public UserException(Throwable ex) {
        super(ExceptionUtil.getMessage(ex), ex);
    }
}
