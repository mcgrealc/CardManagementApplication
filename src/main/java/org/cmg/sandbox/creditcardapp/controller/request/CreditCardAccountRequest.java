package org.cmg.sandbox.creditcardapp.controller.request;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreditCardAccountRequest {
	
	@NotBlank
	@Schema(name="cardNumber", description="Credit card number for the account.", example="4444333322221111")
	private String cardNumber;
	
	@NotBlank
	@Schema(name="cardHolderName", description="Name of cardholder.", example="John Smith")
	private String cardHolderName;
	
	@NotBlank
	@Schema(name="cardExpiry", description="Expiration date for the credit card.", example="12/28")
	private String cardExpiry; 
	
    @NotBlank
	@Schema(name="creditLimit", description="Credit limit for the account.", example="900")
    private BigDecimal creditLimit;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardExpiry() {
		return cardExpiry;
	}

	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public CreditCardAccountRequest(@NotBlank String cardNumber, @NotBlank String cardHolderName,
			@NotBlank String cardExpiry, @NotBlank BigDecimal creditLimit) {
		super();
		this.cardNumber = cardNumber;
		this.cardHolderName = cardHolderName;
		this.cardExpiry = cardExpiry;
		this.creditLimit = creditLimit;
	}

	public CreditCardAccountRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardExpiry, cardHolderName, cardNumber, creditLimit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardAccountRequest other = (CreditCardAccountRequest) obj;
		return Objects.equals(cardExpiry, other.cardExpiry) && Objects.equals(cardHolderName, other.cardHolderName)
				&& Objects.equals(cardNumber, other.cardNumber) && Objects.equals(creditLimit, other.creditLimit);
	}

	@Override
	public String toString() {
		return "CreditCardAccountRequest [cardNumber=" + cardNumber + ", cardHolderName=" + cardHolderName
				+ ", cardExpiry=" + cardExpiry + ", creditLimit=" + creditLimit + "]";
	}
    
    

}
