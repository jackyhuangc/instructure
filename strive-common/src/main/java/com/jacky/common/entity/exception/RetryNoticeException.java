package com.jacky.common.entity.exception;

public class RetryNoticeException extends RuntimeException {

	private static final long serialVersionUID = 4452883432380968183L;

	public RetryNoticeException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public RetryNoticeException(String msg, Exception ex) {
		super(msg, ex);
	}
}
