package org.cmg.sandbox.creditcardapp.controller.response;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class JwtResponseTest {
    @Test
    public void equalsContract()
    {
        EqualsVerifier.simple().forClass(JwtResponse.class).verify();
    }

}
