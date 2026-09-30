package com.library.exception;

public class AlreadyBorrowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyBorrowedException(String msg)
	{
		super(msg);
	}
}
