package com.jacky.strive.common.entity;

public class FailException extends RuntimeException {
    private static final long serialVersionUID = -4189093016903226666L;

    public FailException(String msg, Object... args) {
        super(String.format(msg, args));
    }

    public FailException(String msg, Exception ex) {
        super(msg, ex);
    }
}
