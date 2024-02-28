package org.cmg.sandbox.creditcardapp.exception;

public class AccountSaveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6345409622226185925L;
	
	public AccountSaveException(String errorMessage) {
		super(errorMessage);
	}

}
