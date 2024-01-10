package com.exceptions;

public class FieldValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public FieldValidationException(String message) {
        super(message);
    }
}