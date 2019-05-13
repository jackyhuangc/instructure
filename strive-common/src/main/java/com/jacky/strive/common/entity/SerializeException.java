package com.jacky.strive.common.entity;


/**
 * @Author:denny
 * @Description:
 * @Date:2018/5/18
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg, Exception ex) {
        super(msg, ex);
    }
}
