package org.cmg.sandbox.creditcardapp.model.paymentgateway;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardNumberChargeResponse {
	
    @JsonProperty("paymentStatus")
	private String paymentStatus;
    
    @JsonProperty("paymentStatusReason")
	private String paymentStatusReason;
	
    @JsonProperty("amountCharged")
	private BigDecimal amountCharged;
	
    @JsonProperty("cardEnding")
	private String cardEnding;

	public CardNumberChargeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public CardNumberChargeResponse(String paymentStatus, String paymentStatusReason, BigDecimal amountCharged,
			String cardEnding) {
		super();
		this.paymentStatus = paymentStatus;
		this.paymentStatusReason = paymentStatusReason;
		this.amountCharged = amountCharged;
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

	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}

	public String getCardEnding() {
		return cardEnding;
	}

	public void setCardEnding(String cardEnding) {
		this.cardEnding = cardEnding;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amountCharged, cardEnding, paymentStatus, paymentStatusReason);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardNumberChargeResponse other = (CardNumberChargeResponse) obj;
		return Objects.equals(amountCharged, other.amountCharged) && Objects.equals(cardEnding, other.cardEnding)
				&& Objects.equals(paymentStatus, other.paymentStatus)
				&& Objects.equals(paymentStatusReason, other.paymentStatusReason);
	}

	@Override
	public String toString() {
		return "CardNumberChargeResponse [paymentStatus=" + paymentStatus + ", paymentStatusReason="
				+ paymentStatusReason + ", amountCharged=" + amountCharged + ", cardEnding=" + cardEnding + "]";
	}
	
}
