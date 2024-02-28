package org.cmg.sandbox.creditcardapp.model.paymentgateway;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardNumberCredit {
	
	@JsonProperty("cardNumber")
	private String cardNumber;
	
	@JsonProperty("amount")
	private BigDecimal amount;

	public CardNumberCredit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CardNumberCredit(String cardNumber, BigDecimal amount) {
		super();
		this.cardNumber = cardNumber;
		this.amount = amount;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, cardNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardNumberCredit other = (CardNumberCredit) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(cardNumber, other.cardNumber);
	}

	@Override
	public String toString() {
		return "CardNumberCharge [cardNumber=" + cardNumber + ", amount=" + amount + "]";
	}
	
	

}
