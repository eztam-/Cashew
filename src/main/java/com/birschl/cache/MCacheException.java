package com.birschl.cache;

public class MCacheException extends Exception {

	private static final long serialVersionUID = 3591463138341587636L;

	public MCacheException(Exception e) {
		super(e);
	}

	public MCacheException(String message) {
		super(message);
	}

	public MCacheException(String message, Exception e) {
		super(message, e);
	}

}
