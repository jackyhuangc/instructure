package com.jacky.strive.common.entity;

public class RetryException extends RuntimeException {

	private static final long serialVersionUID = -8650146070639692262L;

	public RetryException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public RetryException(String msg, Exception ex) {
		super(msg, ex);
	}
}
