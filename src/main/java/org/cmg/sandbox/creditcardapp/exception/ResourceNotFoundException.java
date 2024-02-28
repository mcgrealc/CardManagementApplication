package org.cmg.sandbox.creditcardapp.exception;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2320405538783006068L;
	
	public ResourceNotFoundException(final String message) {
		super(message);
	}

}
