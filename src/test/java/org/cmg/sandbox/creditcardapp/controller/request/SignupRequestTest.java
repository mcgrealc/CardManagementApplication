package org.cmg.sandbox.creditcardapp.controller.request;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class SignupRequestTest {
	
    @Test
    public void equalsContract()
    {
        EqualsVerifier.simple().forClass(SignupRequest.class).verify();
    }

}
