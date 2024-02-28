package org.cmg.sandbox.creditcardapp.controller.response;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreditCardAccountResponse {
	
	  @Schema(name="cardId", description="Identifier for the card account which can be used in future operations (if it belongs to the account).", example="123")
	  private long cardId;
	  
	  @Schema(name="cardNumber", description="Last 4 digits of credit card number for the account.", example="************1111")
	  private String cardNumberEnding;
	  
	  @Schema(name="cardHolderName", description="Name of cardholder.", example="John Smith")
	  private String cardHolderName;
	  
	  @Schema(name="creditLimit", description="Credit limit for the account.", example="900")
	  private BigDecimal creditLimit;

	public CreditCardAccountResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditCardAccountResponse(long cardId, String cardNumberEnding, String cardHolderName,
			BigDecimal creditLimit) {
		super();
		this.cardId = cardId;
		this.cardNumberEnding = cardNumberEnding;
		this.cardHolderName = cardHolderName;
		this.creditLimit = creditLimit;
	}

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

	public String getCardNumberEnding() {
		return cardNumberEnding;
	}

	public void setCardNumberEnding(String cardNumberEnding) {
		this.cardNumberEnding = cardNumberEnding;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	@Override
	public String toString() {
		return "CreditCardAccountResponse [cardId=" + cardId + ", cardNumberEnding=" + cardNumberEnding
				+ ", cardHolderName=" + cardHolderName + ", creditLimit=" + creditLimit + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardHolderName, cardId, cardNumberEnding, creditLimit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardAccountResponse other = (CreditCardAccountResponse) obj;
		return Objects.equals(cardHolderName, other.cardHolderName) && cardId == other.cardId
				&& Objects.equals(cardNumberEnding, other.cardNumberEnding)
				&& Objects.equals(creditLimit, other.creditLimit);
	}
	  
	  
	  


}
