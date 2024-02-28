package org.cmg.sandbox.creditcardapp.controller.request;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreditCardAccountCreditLimitUpdateRequest {
	
    @NotBlank
    @Schema(name="creditLimit", description="Additional credit limit for the account", example="900.00")
    private BigDecimal creditLimit;

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public CreditCardAccountCreditLimitUpdateRequest(@NotBlank BigDecimal creditLimit) {
		super();
		this.creditLimit = creditLimit;
	}

	public CreditCardAccountCreditLimitUpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditLimit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardAccountCreditLimitUpdateRequest other = (CreditCardAccountCreditLimitUpdateRequest) obj;
		return Objects.equals(creditLimit, other.creditLimit);
	}

	@Override
	public String toString() {
		return "CreditCardAccountCreditLimitUpdateRequest [creditLimit=" + creditLimit + "]";
	}
	
	
	
	

}
