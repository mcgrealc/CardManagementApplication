package org.cmg.sandbox.creditcardapp.controller.response;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CreditCardAccountResponseTest {
	
    @Test
    public void equalsContract()
    {
        EqualsVerifier.simple().suppress(Warning.BIGDECIMAL_EQUALITY).forClass(CreditCardAccountResponse.class).verify();
    }

}
