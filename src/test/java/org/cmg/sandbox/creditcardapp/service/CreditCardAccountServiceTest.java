package org.cmg.sandbox.creditcardapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cmg.sandbox.creditcardapp.exception.AccountLookupException;
import org.cmg.sandbox.creditcardapp.exception.AccountSaveException;
import org.cmg.sandbox.creditcardapp.exception.InvalidParameterException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.cmg.sandbox.creditcardapp.validators.CreditCardValidatorInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;

/**
 * 
 */
/**
 * 
 */
@ExtendWith(MockitoExtension.class)
public class CreditCardAccountServiceTest {
	
	private static final long TEST_ID = 100l;

	private static final String CARD_HOLDER_NAME = "Test User";

	private static final String CARD_EXPIRY = "12/34";

	private static final String TEST_USER_GMAIL_COM = "testUser@gmail.com";

	private static final String TEST_USER_NAME = "testUserName";

	private static final BigDecimal _9999 = BigDecimal.valueOf(99.99);
	
	private static final BigDecimal _19999 = BigDecimal.valueOf(199.99);
	
	private static final String TEST_CARD_VISA = "4444333322221111";
	
	private static final String TEST_CARD_MC = "5555444433331111";
	
	private static final String TEST_CARD_VISA_INVALID = "444433332222111";
	
	private static final String TEST_CARD_AMEX = "340403934917001";

	@InjectMocks
    private CreditCardAccountService service;

    @Mock
    private CreditCardAccountRepository mockCreditCardAccountRepository;
    
    @Mock
	private CreditCardValidatorInterface mockCreditCardValidator;
    
    
    @Test
    public void givenCreditCardAccountService_whenSaveCreditCardAccount_thenCreditCardAccountResponse() throws Exception {
    	
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        when(mockCreditCardValidator.isValid(TEST_CARD_MC)).thenReturn(true);
        
        when(mockCreditCardAccountRepository.save(testCreditCardAccount)).thenReturn(testCreditCardAccount);
        
        CreditCardAccount result = service.saveCreditCardAccount(testCreditCardAccount);
        
        assertEquals(result, testCreditCardAccount);  	
    }
    
    @Test
    public void givenCreditCardAccountService_whenSaveCreditCardAccount_thenAccountSaveExceptionThrown() throws Exception {
    	try {
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        when(mockCreditCardValidator.isValid(TEST_CARD_MC)).thenReturn(true);
        
        when(mockCreditCardAccountRepository.save(testCreditCardAccount)).thenThrow(new UncategorizedScriptException("DB Gone!"));
        
        service.saveCreditCardAccount(testCreditCardAccount);
    	} catch(AccountSaveException ase) {
    		
    		assertEquals("Failed to save creditCardAccount", ase.getMessage());  	
    	}
        
    }
    
    @Test
    public void givenCreditCardAccountService_whenSaveAmexCreditCardAccount_thenInvalidParameterExceptionThrown() throws Exception {
    	InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                ()->{
                		final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
                		final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_AMEX, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
                		service.saveCreditCardAccount(testCreditCardAccount);
                });
    	
    	assertEquals("Credit card must be Visa or Mastercard", thrown.getMessage());
    }
    
    @Test
    public void givenCreditCardAccountService_whenSaveZeroCreditLimitCreditCardAccount_thenInvalidParameterExceptionThrown() throws Exception {
    	InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                ()->{
                		final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
                		final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_VISA, CARD_EXPIRY, CARD_HOLDER_NAME, BigDecimal.ZERO);
        
                		service.saveCreditCardAccount(testCreditCardAccount);
                }); 
    	
    	assertEquals("Credit limit must be greater than zero", thrown.getMessage());
    }
    
    @Test
    public void givenCreditCardAccountService_whenEmptyCardHolderNameCreditCardAccount_thenInvalidParameterExceptionThrown() throws Exception {
    	InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                ()->{
                		final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
                		final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_VISA, CARD_EXPIRY, "", _9999);
        
                		service.saveCreditCardAccount(testCreditCardAccount);
                }); 
    	
    	assertEquals("Cardholder name is not valid", thrown.getMessage());
    }
    
    @Test
    public void givenCreditCardAccountService_whenInvalidCreditCardNumberProvided_thenInvalidParameterExceptionThrown() throws Exception {
    	InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                ()->{
                		final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
                		final CreditCardAccount testCreditCardAccount = new CreditCardAccount(testUser, TEST_CARD_VISA_INVALID, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
                		service.saveCreditCardAccount(testCreditCardAccount);
                }); 
    	
    	assertEquals("Credit card number is not valid", thrown.getMessage());
    }
    
    @Test
    public void givenCreditCardAccountService_whenFetchAllCreditCardAccounts_thenAccountLookupExceptionThrown() throws Exception {
        try {
    	final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        
        final CreditCardAccount testCreditCardAccountVisa = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        final List<CreditCardAccount> repoResult = new ArrayList<>();
        
        repoResult.add(testCreditCardAccountVisa);
        repoResult.add(testCreditCardAccountMC);
        
        when(mockCreditCardAccountRepository.findByUserId(TEST_ID)).thenThrow(new UncategorizedScriptException("DB Gone!"));
        
        service.fetchAllCreditCardAccounts(TEST_ID);
    	
        } catch(AccountLookupException ale) {
    		
    		assertEquals("Failed to fetch all creditCardAccounts", ale.getMessage());  	
    	}
                     	
        }
    
    @Test
    public void givenCreditCardAccountService_whenFetchAllCreditCardAccounts_thenListCreditCardAccountResponse() throws Exception {
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        
        final CreditCardAccount testCreditCardAccountVisa = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        final List<CreditCardAccount> repoResult = new ArrayList<>();
        
        repoResult.add(testCreditCardAccountVisa);
        repoResult.add(testCreditCardAccountMC);
        
        when(mockCreditCardAccountRepository.findByUserId(TEST_ID)).thenReturn(repoResult);
        
        final List<CreditCardAccount> result = service.fetchAllCreditCardAccounts(TEST_ID);
        
        assertEquals(repoResult, result);
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenFetchCreditCardAccountById_thenCreditCardAccountResponse() throws Exception {
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        
        when(mockCreditCardAccountRepository.findById(TEST_ID)).thenReturn(Optional.of(testCreditCardAccountMC));
        
        final Optional<CreditCardAccount> result = service.fetchCreditCardAccountById(TEST_ID);
        
        assertEquals(testCreditCardAccountMC, result.get());
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenFetchCreditCardAccountById_thenAccountLookupExceptionThrown() throws Exception {
        try {
            final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
            
            final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
            
            
            when(mockCreditCardAccountRepository.findById(TEST_ID)).thenThrow(new UncategorizedScriptException("DB Gone!"));
            
           service.fetchCreditCardAccountById(TEST_ID);
    	
        } catch(AccountLookupException ale) {
    		
    		assertEquals("Failed to fetch CreditCardAccount by ID", ale.getMessage());  	
    	}
                     	
        }
    
    @Test
    public void givenCreditCardAccountService_whenFetchCreditCardAccountByCardNumber_thenCreditCardAccountResponse() throws Exception {
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        
        when(mockCreditCardAccountRepository.findByCardNumber(TEST_CARD_MC)).thenReturn(Optional.of(testCreditCardAccountMC));
        
        final Optional<CreditCardAccount> result = service.fetchCreditCardAccountByCardNumber(TEST_CARD_MC);
        
        assertEquals(testCreditCardAccountMC, result.get());
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenFetchCreditCardAccountByCardNumber_thenAccountLookupExceptionThrown() throws Exception {
        try {
           
            when(mockCreditCardAccountRepository.findByCardNumber(TEST_CARD_MC)).thenThrow(new UncategorizedScriptException("DB Gone!"));
            
            service.fetchCreditCardAccountByCardNumber(TEST_CARD_MC);
    	
        } catch(AccountLookupException ale) {
    		
    		assertEquals("Failed to fetch CreditCardAccount by Card Number", ale.getMessage());  	
    	}
                     	
        }
    
    @Test
    public void givenCreditCardAccountService_whenUpdateCreditCardAccountCreditLimit_thenCreditCardAccountResponse() throws Exception {
        final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
        
        final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
        
        when(mockCreditCardAccountRepository.findById(TEST_ID)).thenReturn(Optional.of(testCreditCardAccountMC));
        
        final CreditCardAccount testUpdatedCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _19999);

        when(mockCreditCardAccountRepository.save(testUpdatedCreditCardAccountMC)).thenReturn(testUpdatedCreditCardAccountMC);
        
        final Optional<CreditCardAccount> result = service.updateCreditCardAccountCreditLimit(TEST_ID, _19999);
        
        assertEquals(testCreditCardAccountMC.getCreditLimit(), result.get().getCreditLimit());
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenUpdateUnknownCreditCardAccountCreditLimit_thenEmptyResponse() throws Exception {
                
        when(mockCreditCardAccountRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        final Optional<CreditCardAccount> result = service.updateCreditCardAccountCreditLimit(TEST_ID, _19999);
        
        assertEquals(Optional.empty(), result);
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenUpdateCreditCardAccountCreditLimit_thenAccountLookupExceptionThrown() throws Exception {
        try {
           
            final User testUser = new User(TEST_USER_NAME, TEST_USER_GMAIL_COM);
            
            final CreditCardAccount testCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _9999);
            
            when(mockCreditCardAccountRepository.findById(TEST_ID)).thenReturn(Optional.of(testCreditCardAccountMC));
            
            final CreditCardAccount testUpdatedCreditCardAccountMC = new CreditCardAccount(testUser, TEST_CARD_MC, CARD_EXPIRY, CARD_HOLDER_NAME, _19999);

            when(mockCreditCardAccountRepository.save(testUpdatedCreditCardAccountMC)).thenThrow(new UncategorizedScriptException("DB Gone!"));
            
            service.updateCreditCardAccountCreditLimit(TEST_ID, _19999);
    	
        } catch(AccountLookupException ale) {
    		
    		assertEquals("Failed to update CreditCardAccount", ale.getMessage());  	
    	}
                     	
        }
    
    @Test
    public void givenCreditCardAccountService_whenDeleteCreditCardAccountByCardNumber_thenBooleanResponse() throws Exception {               
        
        doNothing().when(mockCreditCardAccountRepository).deleteById(100l);
        
        final boolean result = service.deleteCreditCardAccount(TEST_ID);
        
        assertEquals(true, result);
                	
        }
    
    @Test
    public void givenCreditCardAccountService_whenDeleteCreditCardAccountByCardNumber_thenAccountLookupExceptionThrown() throws Exception {
        try {
           
            doThrow(new UncategorizedScriptException("DB Gone!")).when(mockCreditCardAccountRepository).deleteById(TEST_ID);
            
            service.deleteCreditCardAccount(TEST_ID);
    	
        } catch(AccountLookupException ale) {
    		
    		assertEquals("Failed to delete CreditCardAccount", ale.getMessage());  	
    	}
                     	
        }
    
    
    }



