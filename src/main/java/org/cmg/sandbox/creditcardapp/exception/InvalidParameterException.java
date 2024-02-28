package org.cmg.sandbox.creditcardapp.exception;

public class InvalidParameterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629001827430665407L;
	
	public InvalidParameterException(final String errorMessage) {
		super(errorMessage);
	}

}
