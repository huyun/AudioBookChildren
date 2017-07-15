package com.yun.android.provider.exceptions;

public class NDULibraryException extends Exception {

	private static final long serialVersionUID = 2577971150298344276L;

	public NDULibraryException(String message, Throwable exception)
	{
		super(message, exception);
	}
	
	public NDULibraryException(String message)
	{
		super(message);
	}
}
