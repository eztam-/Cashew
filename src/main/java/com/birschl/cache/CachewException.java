package com.birschl.cache;

public class CachewException extends Exception {

	private static final long serialVersionUID = 3591463138341587636L;

	public CachewException(Exception e) {
		super(e);
	}

	public CachewException(String message) {
		super(message);
	}

	public CachewException(String message, Exception e) {
		super(message, e);
	}

}
