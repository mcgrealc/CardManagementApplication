package org.cmg.sandbox.creditcardapp.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.cmg.sandbox.creditcardapp.exception.PaymentGatewayException;
import org.cmg.sandbox.creditcardapp.exception.ResourceNotFoundException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCharge;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberChargeResponse;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCredit;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCreditResponse;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentGatewayService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayService.class);
	
	@Autowired
	CreditCardAccountRepository creditCardAccountRepository;
	
	@Autowired
	RestTemplate restTemplate;

	
    public CardNumberChargeResponse chargeCreditCardAccount(String creditCardNumber, BigDecimal amount) {
    	
    	CardNumberChargeResponse cardNumberChargeResponse = new CardNumberChargeResponse();
    	
        try {
        	CreditCardAccount existingCreditCardAccount = creditCardAccountRepository.findByCardNumber(creditCardNumber).orElseThrow(() -> new ResourceNotFoundException(String.format("No record available for Credit Card ending [%s]", creditCardLastFour(creditCardNumber))));

                if((existingCreditCardAccount.getCreditLimit().subtract(amount)).signum() < 0) {
                	cardNumberChargeResponse.setPaymentStatus("Failure");
                	cardNumberChargeResponse.setPaymentStatusReason("Amount exceeds Credit Limit");
                	cardNumberChargeResponse.setAmountCharged(BigDecimal.ZERO);
                	cardNumberChargeResponse.setCardEnding(creditCardLastFour(creditCardNumber));
                } else {
                
                CardNumberCharge cardNumberCharge = new CardNumberCharge(existingCreditCardAccount.getCardNumber(), amount);
                
                 cardNumberChargeResponse = postChargeToPaymentGateway(cardNumberCharge);
                
                if(cardNumberChargeResponse.getPaymentStatus().equals("Success")) {
                	final BigDecimal updatedCreditLimit = existingCreditCardAccount.getCreditLimit().subtract(amount);
                	existingCreditCardAccount.setCreditLimit(updatedCreditLimit);
                	creditCardAccountRepository.save(existingCreditCardAccount);
                }    
            }
            
        } catch (ResourceNotFoundException rnfe) {
            // Handle exception or log the error
        	logger.debug("Failed to charge CreditCardAccount - reason [{}]", rnfe.getMessage());
        	
            throw new PaymentGatewayException("Failed to charge CreditCardAccount");
        }
        return cardNumberChargeResponse;
    }
    
    public CardNumberCreditResponse creditCreditCardAccount(String creditCardNumber, BigDecimal amount) {
    	
    	CardNumberCreditResponse cardNumberCreditResponse = new CardNumberCreditResponse();
    	
        try {
        	CreditCardAccount existingCreditCardAccount = creditCardAccountRepository.findByCardNumber(creditCardNumber).orElseThrow(() -> new ResourceNotFoundException(String.format("No record available for Credit Card ending [%s]", creditCardLastFour(creditCardNumber))));
                
      
                CardNumberCredit cardNumberCredit = new CardNumberCredit(existingCreditCardAccount.getCardNumber(), amount);
                
                cardNumberCreditResponse = postCreditToPaymentGateway(cardNumberCredit);
                
                if(cardNumberCreditResponse.getPaymentStatus().equals("Success")) {
                	final BigDecimal updatedCreditLimit = existingCreditCardAccount.getCreditLimit().add(amount);
                	existingCreditCardAccount.setCreditLimit(updatedCreditLimit);
                	creditCardAccountRepository.save(existingCreditCardAccount);
                }    
            
            
        } catch (ResourceNotFoundException rnfe) {
            // Handle exception or log the error
        	logger.debug("Failed to credit CreditCardAccount - reason [{}]", rnfe.getMessage());
        	
            throw new PaymentGatewayException("Failed to credit CreditCardAccount");
        }
        return cardNumberCreditResponse;
    }
    
    private CardNumberCreditResponse postCreditToPaymentGateway(CardNumberCredit cardNumberCredit) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<CardNumberCredit> entity = new HttpEntity<CardNumberCredit>(cardNumberCredit,headers);
        
        return restTemplate.postForObject(
           "/creditcard/credit", entity, CardNumberCreditResponse.class);
	}

	private CardNumberChargeResponse postChargeToPaymentGateway(CardNumberCharge cardNumberCharge) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<CardNumberCharge> entity = new HttpEntity<CardNumberCharge>(cardNumberCharge,headers);
        
        return restTemplate.postForObject(
           "/creditcard/charge",  entity, CardNumberChargeResponse.class);
    }
	
	private String creditCardLastFour(String creditCardNumber) {
		return creditCardNumber.substring(creditCardNumber.length() -4, creditCardNumber.length());
	}

}
