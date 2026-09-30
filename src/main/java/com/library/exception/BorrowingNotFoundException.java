package com.library.exception;

public class BorrowingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BorrowingNotFoundException(String message) {
        super(message);
    }
}