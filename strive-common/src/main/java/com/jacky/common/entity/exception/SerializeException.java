package com.jacky.common.entity.exception;


/**
 * @Author:denny
 * @Description:
 * @Date:2018/5/18
 */
public class SerializeException extends RuntimeException {
    private static final long serialVersionUID = 4426561656006569503L;

    public SerializeException(String msg, Exception ex) {
        super(msg, ex);
    }
}
