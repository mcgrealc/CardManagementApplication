package org.cmg.sandbox.creditcardapp.model.paymentgateway;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardNumberCreditResponse {
	
    @JsonProperty("paymentStatus")
	private String paymentStatus;
    
    @JsonProperty("paymentStatusReason")
	private String paymentStatusReason;
	
    @JsonProperty("amountCredited")
	private BigDecimal amountCredited;
	
    @JsonProperty("cardEnding")
	private String cardEnding;

	public CardNumberCreditResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CardNumberCreditResponse(String paymentStatus, String paymentStatusReason, BigDecimal amountCredited,
			String cardEnding) {
		super();
		this.paymentStatus = paymentStatus;
		this.paymentStatusReason = paymentStatusReason;
		this.amountCredited = amountCredited;
		this.cardEnding = cardEnding;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentStatusReason() {
		return paymentStatusReason;
	}

	public void setPaymentStatusReason(String paymentStatusReason) {
		this.paymentStatusReason = paymentStatusReason;
	}

	public BigDecimal getAmountCredited() {
		return amountCredited;
	}

	public void setAmountCredited(BigDecimal amountCredited) {
		this.amountCredited = amountCredited;
	}

	public String getCardEnding() {
		return cardEnding;
	}

	public void setCardEnding(String cardEnding) {
		this.cardEnding = cardEnding;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amountCredited, cardEnding, paymentStatus, paymentStatusReason);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardNumberCreditResponse other = (CardNumberCreditResponse) obj;
		return Objects.equals(amountCredited, other.amountCredited) && Objects.equals(cardEnding, other.cardEnding)
				&& Objects.equals(paymentStatus, other.paymentStatus)
				&& Objects.equals(paymentStatusReason, other.paymentStatusReason);
	}

	@Override
	public String toString() {
		return "CardNumberCreditResponse [paymentStatus=" + paymentStatus + ", paymentStatusReason="
				+ paymentStatusReason + ", amountCredited=" + amountCredited + ", cardEnding=" + cardEnding + "]";
	}
	
}
