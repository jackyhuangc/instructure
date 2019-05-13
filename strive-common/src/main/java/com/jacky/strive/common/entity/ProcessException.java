package com.jacky.strive.common.entity;

public class ProcessException extends RuntimeException {

	private static final long serialVersionUID = -2288314184495537683L;

	public ProcessException(String msg, Object... args) {
		super(String.format(msg, args));
	}

	public ProcessException(String msg, Exception ex) {
		super(msg, ex);
	}
}
