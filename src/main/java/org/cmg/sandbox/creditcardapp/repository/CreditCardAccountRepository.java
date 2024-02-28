package org.cmg.sandbox.creditcardapp.repository;

import java.util.List;
import java.util.Optional;

import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardAccountRepository extends JpaRepository<CreditCardAccount, Long> {
	
	List<CreditCardAccount> findByUserId(long userId);
	
	Optional<CreditCardAccount> findByCardNumber(String cardNumber);
	
}
