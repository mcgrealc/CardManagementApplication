package org.cmg.sandbox.creditcardapp.exception;

public class PaymentGatewayException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471116122276316559L;
	
	public PaymentGatewayException(final String message) {
		super(message);
	}

}
