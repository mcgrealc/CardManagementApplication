package org.cmg.sandbox.creditcardapp.controller.request;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreditAccountRequest {
	
    @NotBlank
    @Schema(name="amount", description="Amount to credit to the credit card account", example="19.95")
    private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CreditAccountRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditAccountRequest(@NotBlank BigDecimal amount) {
		super();
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditAccountRequest other = (CreditAccountRequest) obj;
		return Objects.equals(amount, other.amount);
	}

	@Override
	public String toString() {
		return "CreditAccountRequest [amount=" + amount + "]";
	}

    
}
