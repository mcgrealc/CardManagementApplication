package org.cmg.sandbox.creditcardapp.encryption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreditCardConvertorTest {
	
	private static final String TEST_STRING = "4444333322221111";
	CreditCardConvertor testClass;
	
	@BeforeEach
	public void setUp() throws Exception {
		testClass = new CreditCardConvertor();
	}
	
	@Test
	public void givenCreditCardConvertor_convertToDatabaseColumn_thenEncryptedStringResponse() throws Exception {
		
		assertNotEquals(TEST_STRING, testClass.convertToDatabaseColumn(TEST_STRING));
	}
	
	@Test
	public void givenCreditCardConvertor_convertToDatabaseColumnAndConvertToEntityAttribute_thenInputMatchesOutput() throws Exception {
		
		final String encrypted = testClass.convertToDatabaseColumn(TEST_STRING);
		
		final String decrypted = testClass.convertToEntityAttribute(encrypted);
		
		assertEquals(TEST_STRING, decrypted);
	}

}
