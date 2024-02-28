package org.cmg.sandbox.creditcardapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.cmg.sandbox.creditcardapp.exception.AccountLookupException;
import org.cmg.sandbox.creditcardapp.exception.AccountSaveException;
import org.cmg.sandbox.creditcardapp.exception.InvalidParameterException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.cmg.sandbox.creditcardapp.validators.CreditCardValidatorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CreditCardAccountService {
	
	private static final Logger logger = LoggerFactory.getLogger(CreditCardAccountService.class);
	
	@Autowired
	CreditCardAccountRepository creditCardAccountRepository;
	
	@Autowired
	CreditCardValidatorInterface creditCardValidator;
	
	public CreditCardAccount saveCreditCardAccount(final CreditCardAccount creditCardAccount) throws InvalidParameterException, AccountSaveException{
		
		if(StringUtils.isEmpty(creditCardAccount.getCardHolderName())){
			logger.warn("Card holder name not valid.");
			throw new InvalidParameterException("Cardholder name is not valid");
		}
		
		if(creditCardAccount.getCreditLimit().signum() != 1) {
			logger.warn("Credit limit must be greater than zero.");
			throw new InvalidParameterException("Credit limit must be greater than zero");
		}
		
		if(creditCardAccount.getCardNumber().startsWith("4") ||  creditCardAccount.getCardNumber().startsWith("5")) {
		
		if(!creditCardValidator.isValid(creditCardAccount.getCardNumber())){
			logger.warn("Credit card not valid.");
			throw new InvalidParameterException("Credit card number is not valid");
		}
        try {
            return creditCardAccountRepository.save(creditCardAccount);
        } catch (DataAccessException e) {
        	logger.warn("Exception saving Credit Card Account [{}]", e.getMessage());

            throw new AccountSaveException("Failed to save creditCardAccount");
        }
		}else {
			logger.warn("Credit card not Visa or MasterCard");
			throw new InvalidParameterException("Credit card must be Visa or Mastercard");
		}
    }
	
    public List<CreditCardAccount> fetchAllCreditCardAccounts(final long id ) {
        try {
            return creditCardAccountRepository.findByUserId(id);
        } catch (DataAccessException e) {
        	logger.warn("Exception fetching Credit Card Accounts [{}]", e.getMessage());
            throw new AccountLookupException("Failed to fetch all creditCardAccounts");
        }
    }
    
    public Optional<CreditCardAccount> fetchCreditCardAccountById(Long id) {
         try {
             return creditCardAccountRepository.findById(id);
         } catch (DataAccessException e) {
        	 logger.warn("Exception fetching Credit Card Account by ID [{}]", e.getMessage());
             throw new AccountLookupException("Failed to fetch CreditCardAccount by ID");
         }
     }
    
    public Optional<CreditCardAccount> fetchCreditCardAccountByCardNumber(String cardNumber) {
        try {
            return creditCardAccountRepository.findByCardNumber(cardNumber);
        } catch (DataAccessException e) {
        	logger.warn("Exception fetching Credit Card Account by Card Number [{}]", e.getMessage());
            throw new AccountLookupException("Failed to fetch CreditCardAccount by Card Number");
        }
    }
    
    public Optional<CreditCardAccount> updateCreditCardAccountCreditLimit(Long id, BigDecimal newCreditLimit) {
        try {
            Optional<CreditCardAccount> existingCreditCardAccountOptional = creditCardAccountRepository.findById(id);
            if (existingCreditCardAccountOptional.isPresent()) {
                CreditCardAccount existingCreditCardAccount = existingCreditCardAccountOptional.get();
                existingCreditCardAccount.setCreditLimit(newCreditLimit);
                CreditCardAccount savedEntity = creditCardAccountRepository.save(existingCreditCardAccount);
                return Optional.of(savedEntity);
            } else {
                return Optional.empty();
            }
        } catch (DataAccessException e) {
        	logger.warn("Exception updating Credit Card Account Credit Limit [{}]", e.getMessage());
            throw new AccountLookupException("Failed to update CreditCardAccount");
        }
    }
     
    public boolean deleteCreditCardAccount(Long id) {
        try {
            creditCardAccountRepository.deleteById(id);
            return true; // Deletion successful
        } catch (DataAccessException e) {
        	logger.warn("Exception deleting Credit Card Account by ID [{}]", e.getMessage());
            throw new AccountLookupException("Failed to delete CreditCardAccount");
        }
    }

}
