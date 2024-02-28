package org.cmg.sandbox.creditcardapp.controller.request;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CreditCardAccountRequestTest {
	
    @Test
    public void equalsContract()
    {
        EqualsVerifier.simple().suppress(Warning.BIGDECIMAL_EQUALITY).forClass(CreditCardAccountRequest.class).verify();
    }

}
