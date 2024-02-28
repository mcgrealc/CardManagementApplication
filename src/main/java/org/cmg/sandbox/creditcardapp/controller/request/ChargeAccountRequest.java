package org.cmg.sandbox.creditcardapp.controller.request;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class ChargeAccountRequest {
	
    @NotBlank
    @Schema(name="amount", description="Amount to charge to the credit card account", example="19.95")
    private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public ChargeAccountRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChargeAccountRequest(@NotBlank BigDecimal amount) {
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
		ChargeAccountRequest other = (ChargeAccountRequest) obj;
		return Objects.equals(amount, other.amount);
	}

	@Override
	public String toString() {
		return "ChargeAccountRequest [amount=" + amount + "]";
	}

    
}
