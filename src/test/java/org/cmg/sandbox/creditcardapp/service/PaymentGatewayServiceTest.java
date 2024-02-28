package org.cmg.sandbox.creditcardapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.cmg.sandbox.creditcardapp.exception.PaymentGatewayException;
import org.cmg.sandbox.creditcardapp.exception.ResourceNotFoundException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCharge;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberChargeResponse;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCredit;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCreditResponse;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class PaymentGatewayServiceTest {
	
	
    private static final BigDecimal _9999 = BigDecimal.valueOf(99.99);

	private static final BigDecimal _2399 = BigDecimal.valueOf(23.99);
	
	private static final BigDecimal _1099 = BigDecimal.valueOf(10.99);

	private static final String TEST_VISA_CARD = "4444333322221111";

	@InjectMocks
    private PaymentGatewayService service;

    @Mock
    private CreditCardAccountRepository mockCreditCardAccountRepository;
	
    @Mock
    private RestTemplate mockRestTemplate;
	 		
    @Test
    public void givenPaymentGatewayService_whenChargeCreditCardAccount_thenCardNumberChargeResponse() throws Exception {
    
    final User testUser = new User("testUserName", "testUser@gmail.com");
    
    final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_VISA_CARD, "12/34", "Test User", _9999);
    
    CardNumberCharge cardNumberCharge = new CardNumberCharge(TEST_VISA_CARD, _2399);
    	
    when(mockCreditCardAccountRepository.findByCardNumber(TEST_VISA_CARD)).thenReturn(Optional.of(testCreditCardAccount));
    
    CardNumberChargeResponse cardNumberChargeResponse = new CardNumberChargeResponse("Success", null, _2399, "1111");
    
	HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<CardNumberCharge> entity = new HttpEntity<CardNumberCharge>(cardNumberCharge,headers);
    
    when(mockRestTemplate.postForObject("/creditcard/charge", entity,CardNumberChargeResponse.class)).thenReturn(cardNumberChargeResponse);
    
    final CardNumberChargeResponse response =  service.chargeCreditCardAccount(TEST_VISA_CARD, _2399);
    
    assertEquals(response.getAmountCharged(), _2399);
    assertEquals(response.getCardEnding(), "1111");
    assertEquals(response.getPaymentStatus(), "Success");
    assertEquals(response.getPaymentStatusReason(), null);
    }
    
    @Test
    public void givenPaymentGatewayService_whenChargeCreditCardAccount_thenCreditLimitExceededResponse() throws Exception {
    
    final User testUser = new User("testUserName", "testUser@gmail.com");
    
    final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_VISA_CARD, "12/34", "Test User", _1099);
        	
    when(mockCreditCardAccountRepository.findByCardNumber(TEST_VISA_CARD)).thenReturn(Optional.of(testCreditCardAccount));
    
    final CardNumberChargeResponse response =  service.chargeCreditCardAccount(TEST_VISA_CARD, _2399);
    
    assertEquals(response.getAmountCharged(), BigDecimal.ZERO);
    assertEquals(response.getCardEnding(), "1111");
    assertEquals(response.getPaymentStatus(), "Failure");
    assertEquals(response.getPaymentStatusReason(), "Amount exceeds Credit Limit");
    }
    
    @Test
    public void givenPaymentGatewayService_whenChargeCreditCardAccount_thenExceptionThrown() throws Exception {
        assertThrows(PaymentGatewayException.class,
                ()->{
                    when(mockCreditCardAccountRepository.findByCardNumber(TEST_VISA_CARD)).thenThrow(new ResourceNotFoundException("Test") );
                    
                    service.chargeCreditCardAccount(TEST_VISA_CARD, _2399);
                });
    	
    }
    
    @Test
    public void givenPaymentGatewayService_whenCreditCreditCardAccount_thenCardNumberCreditResponse() throws Exception {
    
    final User testUser = new User("testUserName", "testUser@gmail.com");
    
    final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_VISA_CARD, "12/34", "Test User", _9999);
    
    CardNumberCredit cardNumberCredit = new CardNumberCredit(TEST_VISA_CARD, _2399);
    	
    when(mockCreditCardAccountRepository.findByCardNumber(TEST_VISA_CARD)).thenReturn(Optional.of(testCreditCardAccount));
    
    CardNumberCreditResponse cardNumberCreditResponse = new CardNumberCreditResponse("Success", null, _2399, "1111");
    
	HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<CardNumberCredit> entity = new HttpEntity<CardNumberCredit>(cardNumberCredit,headers);
    
    when(mockRestTemplate.postForObject("/creditcard/credit", entity, CardNumberCreditResponse.class)).thenReturn(cardNumberCreditResponse);
    
    final CardNumberCreditResponse response =  service.creditCreditCardAccount(TEST_VISA_CARD, _2399);
    
    assertEquals(response.getAmountCredited(), _2399);
    assertEquals(response.getCardEnding(), "1111");
    assertEquals(response.getPaymentStatus(), "Success");
    assertEquals(response.getPaymentStatusReason(), null);
    }
    
    @Test
    public void givenPaymentGatewayService_whenCreditCreditCardAccount_thenExceptionThrown() throws Exception {
        assertThrows(PaymentGatewayException.class,
                ()->{
                		when(mockCreditCardAccountRepository.findByCardNumber(TEST_VISA_CARD)).thenThrow(new ResourceNotFoundException("Test") );
   
                	 service.creditCreditCardAccount(TEST_VISA_CARD, _2399);
                });
    }

}
