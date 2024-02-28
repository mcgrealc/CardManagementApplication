package org.cmg.sandbox.creditcardapp.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreditCardValidatorTest {
	
	CreditCardValidator testClass;
	
	@BeforeEach
	public void setUp() throws Exception {
		testClass = new CreditCardValidator();
	}
	
	@Test
	public void givenCreditCardValidator_whenValidVisaCardValidated_thenAffirmativeResponse() throws Exception {
		
		assertTrue(testClass.isValid("4444333322221111"));
	}
	
	@Test
	public void givenCreditCardValidator_whenValidMCCardValidated_thenAffirmativeResponse() throws Exception {
		
		assertTrue(testClass.isValid("5555444433331111"));
	}
	
	@Test
	public void givenCreditCardValidator_whenInvalidMCCardValidated_thenNegativeResponse() throws Exception {
		
		assertFalse(testClass.isValid("555544443333111123456778"));
	}
	
	@Test
	public void givenCreditCardValidator_whenInvalidLengthVisaCardValidated_thenNegativeResponse() throws Exception {
		
		assertFalse(testClass.isValid("44443333222"));
	}
	
	@Test
	public void givenCreditCardValidator_whenInvalidMod10VisaCardValidated_thenNegativeResponse() throws Exception {
		
		assertFalse(testClass.isValid("444433332221234"));
	}

}
