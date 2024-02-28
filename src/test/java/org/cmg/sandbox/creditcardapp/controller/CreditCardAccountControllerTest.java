package org.cmg.sandbox.creditcardapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.cmg.sandbox.creditcardapp.controller.request.ChargeAccountRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditAccountRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditCardAccountCreditLimitUpdateRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditCardAccountRequest;
import org.cmg.sandbox.creditcardapp.exception.AccountSaveException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberChargeResponse;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCreditResponse;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.cmg.sandbox.creditcardapp.repository.UserRepository;
import org.cmg.sandbox.creditcardapp.service.CreditCardAccountService;
import org.cmg.sandbox.creditcardapp.service.PaymentGatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CreditCardAccountController.class)
public class CreditCardAccountControllerTest {
	
    private static final String TEST_USERNAME_INVALID = "Bad Actor";

	private static final long LOOKUP_ID = 100l;

	private static final String TEST_USERNAME_VALID = "User Name";

	private static final String TEST_CARD_NUMBER_VISA = "4444333322221111";

	private static final String TEST_EXPIRY = "12/30";

	private static final String TEST_CARDHOLDER_NAME = "Tom Thumb";

	private static final BigDecimal TEST_CREDIT_LIMIT_VISA = BigDecimal.valueOf(100.00);

	private static final String TEST_EMAIL_VALID = "test@gmail.com";

	private static final BigDecimal TEST_CHARGE_ACCOUNT_AMOUNT = BigDecimal.valueOf(25.00);


	@Autowired
    private MockMvc             mockMvc;
    
    @Autowired
    @MockBean
    CreditCardAccountRepository mockCreditCardAccountRepository;
    
    @Autowired
    @MockBean
    CreditCardAccountService mockCreditCardAccountService;
    
    @Autowired
    @MockBean
    RestTemplate mockRestTemplate;
    
    @Autowired
    @MockBean
    PaymentGatewayService mockPaymentGatewayService;
    
    @Autowired
    @MockBean
    UserRepository mockUserRepository;
    
    User testUser;
    
     
    @BeforeEach
    public void setup() throws Exception{
    	testUser = new User(TEST_USERNAME_VALID, TEST_EMAIL_VALID, null);
    	testUser.setId(LOOKUP_ID);
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenGetIndexedCreditCardAccount_thenCreditCardAccountResponseReturned() throws Exception 
    {
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountById(LOOKUP_ID)).thenReturn(Optional.of(getTestCreditCardAccount()));
    	
        final MvcResult result = mockMvc.perform(get("/api/credit-cards/100"))
                                        .andExpect(status().is2xxSuccessful())
                                        .andReturn();
             	    		  
        assertEquals("{\"cardId\":100,\"cardNumberEnding\":\"************1111\",\"cardHolderName\":\"Tom Thumb\",\"creditLimit\":100.0}", result.getResponse().getContentAsString());
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenGetIndexedCreditCardAccount_thenExceptionErrorMessageReturned() throws Exception 
    {
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountById(LOOKUP_ID)).thenThrow(new ResourceAccessException(TEST_USERNAME_INVALID));
    	
        final MvcResult result = mockMvc.perform(get("/api/credit-cards/100"))
                                        .andExpect(status().isUnauthorized())
                                        .andReturn();
             	    		  
        assertEquals("{\"message\":\"You are not authorized to access this resource\"}", result.getResponse().getContentAsString());
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenGetCreditCardAccounts_thenListOfCreditCardAccountsResponseReturned() throws Exception
    {
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchAllCreditCardAccounts(LOOKUP_ID)).thenReturn(List.of(getTestCreditCardAccount()));
    	
        final MvcResult result = mockMvc.perform(get("/api/credit-cards"))
                                        .andExpect(status().is2xxSuccessful())
                                        .andReturn();
             	    		  
        assertEquals("[{\"cardId\":100,\"cardNumberEnding\":\"************1111\",\"cardHolderName\":\"Tom Thumb\",\"creditLimit\":100.0}]", result.getResponse().getContentAsString());
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenGetCreditCardAccounts_thenExceptionErrorMessageReturned() throws Exception
    {
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchAllCreditCardAccounts(LOOKUP_ID)).thenThrow(new UsernameNotFoundException(TEST_USERNAME_INVALID));
    	
        final MvcResult result = mockMvc.perform(get("/api/credit-cards"))
                                        .andExpect(status().isUnauthorized())
                                        .andReturn();
             	    		  
        assertEquals("{\"message\":\"You are not authorized to access this resource\"}", result.getResponse().getContentAsString());
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenUpdateCreditLimit_then204ResponseReturned() throws Exception
    {
    	
    	CreditCardAccountCreditLimitUpdateRequest testCreditCardAccountCreditLimitUpdateRequest = new CreditCardAccountCreditLimitUpdateRequest(BigDecimal.valueOf(250.00));
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountById(LOOKUP_ID)).thenReturn(Optional.of(getTestCreditCardAccount()));
    	
    	when(mockCreditCardAccountService.updateCreditCardAccountCreditLimit(LOOKUP_ID, testCreditCardAccountCreditLimitUpdateRequest.getCreditLimit())).thenReturn(Optional.of(getTestCreditCardAccount()));
    	
       mockMvc.perform(put("/api/credit-cards/100").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountCreditLimitUpdateRequest)))
                                        .andExpect(status().isNoContent());
                                                 	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenUpdateCreditLimit_thenExceptionErrorMessageReturned() throws Exception
    {
    	
    	CreditCardAccountCreditLimitUpdateRequest testCreditCardAccountCreditLimitUpdateRequest = new CreditCardAccountCreditLimitUpdateRequest(BigDecimal.valueOf(250.00));
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountById(LOOKUP_ID)).thenReturn(Optional.of(getTestCreditCardAccount()));
    	
    	when(mockCreditCardAccountService.updateCreditCardAccountCreditLimit(LOOKUP_ID, testCreditCardAccountCreditLimitUpdateRequest.getCreditLimit())).thenThrow(new ResourceAccessException(TEST_USERNAME_INVALID));
    	
    	final MvcResult result = mockMvc.perform(put("/api/credit-cards/100").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountCreditLimitUpdateRequest)))
                                        .andExpect(status().isUnauthorized()).andReturn();
        assertEquals("{\"message\":\"You are not authorized to access this resource\"}", result.getResponse().getContentAsString());

                                                 	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenDeleteCreditCardAccount_then204ResponseReturned() throws Exception
    {
    	
    	CreditCardAccountCreditLimitUpdateRequest testCreditCardAccountCreditLimitUpdateRequest = new CreditCardAccountCreditLimitUpdateRequest(BigDecimal.valueOf(250.00));
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountById(LOOKUP_ID)).thenReturn(Optional.of(getTestCreditCardAccount()));
    	
    	when(mockCreditCardAccountService.deleteCreditCardAccount(LOOKUP_ID)).thenReturn(true);
    	
       mockMvc.perform(delete("/api/credit-cards/100").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountCreditLimitUpdateRequest)))
                                        .andExpect(status().isNoContent());
                                                 	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenPostNewCreditCardAccount_thenCreditCardAccountResponseReturned() throws Exception
    {
    	
    	CreditCardAccountRequest testCreditCardAccountRequest = new CreditCardAccountRequest(TEST_CARD_NUMBER_VISA, TEST_CARDHOLDER_NAME, TEST_EXPIRY, TEST_CREDIT_LIMIT_VISA);
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);
    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.saveCreditCardAccount(testCreditCardAccount)).thenReturn(testCreditCardAccount);
    	
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountRequest)))
                                        .andExpect(status().isCreated()).andReturn();
    	
        assertEquals("{\"message\":\"Created Credit Card Account with Card ending ************1111\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenPostNewCreditCardAccount_thenAccountNotCreatedResponseReturned() throws Exception
    {
    	
    	CreditCardAccountRequest testCreditCardAccountRequest = new CreditCardAccountRequest(TEST_CARD_NUMBER_VISA, TEST_CARDHOLDER_NAME, TEST_EXPIRY, TEST_CREDIT_LIMIT_VISA);
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);
    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.saveCreditCardAccount(testCreditCardAccount)).thenReturn(null);
    	
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Unable to create Credit Card Account\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenPostNewCreditCardAccount_thenExceptionErrorResponseReturned() throws Exception
    {
    	
    	CreditCardAccountRequest testCreditCardAccountRequest = new CreditCardAccountRequest(TEST_CARD_NUMBER_VISA, TEST_CARDHOLDER_NAME, TEST_EXPIRY, TEST_CREDIT_LIMIT_VISA);
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);
    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	    	
    	when(mockCreditCardAccountService.saveCreditCardAccount(testCreditCardAccount)).thenThrow(new AccountSaveException("Error saving details"));
    	
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditCardAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Unable to create Credit Card Account - reason [Error saving details]\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenChargeCreditCardAccount_thenSuccessfulChargeMessageReturned() throws Exception
    { 	
    	ChargeAccountRequest testChargeAccountRequest = new ChargeAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	
    	CardNumberChargeResponse testCardNumberChargeResponse = new CardNumberChargeResponse("Success", null, TEST_CHARGE_ACCOUNT_AMOUNT, "1111");
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);

    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountByCardNumber(TEST_CARD_NUMBER_VISA)).thenReturn(Optional.of(testCreditCardAccount));
    	    	
    	when(mockPaymentGatewayService.chargeCreditCardAccount(TEST_CARD_NUMBER_VISA, TEST_CHARGE_ACCOUNT_AMOUNT)).thenReturn(testCardNumberChargeResponse);
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/charge").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testChargeAccountRequest)))
                                        .andExpect(status().isOk()).andReturn();
    	
        assertEquals("{\"message\":\"Credit Card Account with card ending [1111] charged with amount [25.00]\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenChargeCreditCardAccount_thenFailedPaymentErrorResponseReturned() throws Exception
    { 	
    	ChargeAccountRequest testChargeAccountRequest = new ChargeAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	
    	CardNumberChargeResponse testCardNumberChargeResponse = new CardNumberChargeResponse("Failure", "Credit Limit Exceeded", BigDecimal.ZERO, "1111");
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);

    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountByCardNumber(TEST_CARD_NUMBER_VISA)).thenReturn(Optional.of(testCreditCardAccount));
    	    	
    	when(mockPaymentGatewayService.chargeCreditCardAccount(TEST_CARD_NUMBER_VISA, TEST_CHARGE_ACCOUNT_AMOUNT)).thenReturn(testCardNumberChargeResponse);
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/charge").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testChargeAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Unable to charge Credit Card Account - reason [Credit Limit Exceeded]\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenChargeCreditCardAccount_thenExceptionErrorResponseReturned() throws Exception
    { 	
    	ChargeAccountRequest testChargeAccountRequest = new ChargeAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenThrow(new UsernameNotFoundException(TEST_USERNAME_INVALID));
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/charge").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testChargeAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
  	
        assertEquals("{\"message\":\"You are not authorized to use this credit card\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenCreditCreditCardAccount_thenSuccessfulCreditMessageReturned() throws Exception
    { 	
    	CreditAccountRequest testCreditAccountRequest = new CreditAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	
    	CardNumberCreditResponse testCardNumberCreditResponse = new CardNumberCreditResponse("Success", null, TEST_CHARGE_ACCOUNT_AMOUNT, "1111");
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);

    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountByCardNumber(TEST_CARD_NUMBER_VISA)).thenReturn(Optional.of(testCreditCardAccount));
    	    	
    	when(mockPaymentGatewayService.creditCreditCardAccount(TEST_CARD_NUMBER_VISA, TEST_CHARGE_ACCOUNT_AMOUNT)).thenReturn(testCardNumberCreditResponse);
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/credit").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditAccountRequest)))
                                        .andExpect(status().isOk()).andReturn();
    	
        assertEquals("{\"message\":\"Credit Card Account with card ending [1111] credited with amount [25.00]\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenCreditCreditCardAccount_thenFailedPaymentErrorResponseReturned() throws Exception
    { 	
    	CreditAccountRequest testCreditAccountRequest = new CreditAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	
    	CardNumberCreditResponse testCardNumberCreditResponse = new CardNumberCreditResponse("Failure", "Credit denied", BigDecimal.ZERO, "1111");
    	
    	CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);

    	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenReturn(Optional.of(testUser));
    	
    	when(mockCreditCardAccountService.fetchCreditCardAccountByCardNumber(TEST_CARD_NUMBER_VISA)).thenReturn(Optional.of(testCreditCardAccount));
    	    	
    	when(mockPaymentGatewayService.creditCreditCardAccount(TEST_CARD_NUMBER_VISA, TEST_CHARGE_ACCOUNT_AMOUNT)).thenReturn(testCardNumberCreditResponse);
    	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/credit").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Unable to credit Credit Card Account - reason [Credit denied]\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenCreditCardAccount_whenCreditCreditCardAccount_thenExceptionErrorResponseReturned() throws Exception
    { 	
    	CreditAccountRequest testCreditAccountRequest = new CreditAccountRequest(TEST_CHARGE_ACCOUNT_AMOUNT);
    	  	
    	when(mockUserRepository.findByUserName(TEST_USERNAME_VALID)).thenThrow(new UsernameNotFoundException(TEST_USERNAME_INVALID));
    	 	
    	final MvcResult result = mockMvc.perform(post("/api/credit-cards/4444333322221111/credit").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(testCreditAccountRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();    	
    	
    	assertEquals("{\"message\":\"You are not authorized to use this credit card\"}", result.getResponse().getContentAsString());                                                	    		  
    }
    
    
    private CreditCardAccount getTestCreditCardAccount(){
    	CreditCardAccount result = new CreditCardAccount(testUser, TEST_CARD_NUMBER_VISA, TEST_EXPIRY, TEST_CARDHOLDER_NAME, TEST_CREDIT_LIMIT_VISA);
    	
    	result.setId(LOOKUP_ID);
    	
    	return(result);
    }
    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
