package org.cmg.sandbox.creditcardapp.model;

import java.math.BigDecimal;
import java.util.Objects;

import org.cmg.sandbox.creditcardapp.encryption.CreditCardConvertor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CREDIT_CARD_ACCOUNTS")
public class CreditCardAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	  @ManyToOne(fetch = FetchType.LAZY, optional = false)
	  @JoinColumn(name = "user_id", nullable = false)
	  @OnDelete(action = OnDeleteAction.CASCADE)
	  @JsonIgnore
	  private User user;
	  
	  @Convert(converter = CreditCardConvertor.class)
	  @Column(unique=true,length=64)
	  private String cardNumber;
	    

	  @Convert(converter = CreditCardConvertor.class)
	  @Column(length=64)
	  private String cardExpiry;
	    
	  @Convert(converter = CreditCardConvertor.class)
	  @Column(length=64)
	  private String cardHolderName;
	    
	  @Column(columnDefinition = "DECIMAL(7,2)")
	  private BigDecimal creditLimit;
	  
	  

	public CreditCardAccount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public CreditCardAccount(User user, String cardNumber, String cardExpiry, String cardHolderName,
			BigDecimal creditLimit) {
		super();
		this.user = user;
		this.cardNumber = cardNumber;
		this.cardExpiry = cardExpiry;
		this.cardHolderName = cardHolderName;
		this.creditLimit = creditLimit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User customer) {
		this.user = customer;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardExpiry() {
		return cardExpiry;
	}

	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
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
	
	public String getMaskedCardLastFour() {
		return String.format("************%s", cardNumber.substring(cardNumber.length() -4, cardNumber.length()));
	}

	@Override
	public String toString() {
		return "CreditCardAccount [id=" + id + ", customer=" + user + ", cardNumber=" + cardNumber + ", cardExpiry="
				+ cardExpiry + ", cardHolderName=" + cardHolderName + ", creditLimit=" + creditLimit + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardExpiry, cardHolderName, cardNumber, creditLimit, id, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardAccount other = (CreditCardAccount) obj;
		return Objects.equals(cardExpiry, other.cardExpiry) && Objects.equals(cardHolderName, other.cardHolderName)
				&& Objects.equals(cardNumber, other.cardNumber) && Objects.equals(creditLimit, other.creditLimit)
				&& id == other.id && Objects.equals(user, other.user);
	}
	
}
