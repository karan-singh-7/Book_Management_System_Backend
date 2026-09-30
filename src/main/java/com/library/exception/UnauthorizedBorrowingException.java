package com.library.exception;

public class UnauthorizedBorrowingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedBorrowingException(String msg)
	{
		super(msg);
	}
}
