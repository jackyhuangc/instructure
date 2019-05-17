package com.jacky.common.entity.exception;

public class FailNoticeException extends RuntimeException {

	private static final long serialVersionUID = -8994288354327034197L;

	public FailNoticeException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public FailNoticeException(String msg, Exception ex) {
		super(msg, ex);
	}

}
