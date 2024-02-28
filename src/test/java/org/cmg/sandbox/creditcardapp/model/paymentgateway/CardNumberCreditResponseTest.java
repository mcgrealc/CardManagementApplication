package org.cmg.sandbox.creditcardapp.model.paymentgateway;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CardNumberCreditResponseTest {
	
    @Test
    public void equalsContract()
    {
        EqualsVerifier.simple().suppress(Warning.BIGDECIMAL_EQUALITY).forClass(CardNumberCreditResponse.class).verify();
    }

}
