package com.yaochufa.apijava.recsys.exception;

public class ConnectionException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionException()
	{
	}

	public ConnectionException(String msg)
	{
		super(msg);
	}

	public ConnectionException(Throwable throwable)
	{
		super(throwable);
	}

	public ConnectionException(String msg, Throwable throwable)
	{
		super(msg, throwable);
	}

}
