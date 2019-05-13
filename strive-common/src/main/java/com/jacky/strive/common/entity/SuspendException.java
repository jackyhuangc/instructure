package com.jacky.strive.common.entity;

public class SuspendException extends RuntimeException {
	
	private static final long serialVersionUID = 4095876420082911413L;

	public SuspendException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public SuspendException(String msg, Exception ex) {
		super(msg, ex);
	}
}
