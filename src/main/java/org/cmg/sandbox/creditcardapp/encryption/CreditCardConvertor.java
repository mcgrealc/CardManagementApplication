package org.cmg.sandbox.creditcardapp.encryption;


import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;

public class CreditCardConvertor implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "D1dg3RXOLq5lAcetnsLFRbErL08eLv0m".getBytes();
 
    @Override
    public String convertToDatabaseColumn(String ccNumber) {
      Key key = new SecretKeySpec(KEY, "AES");
      try {
         Cipher c = Cipher.getInstance(ALGORITHM);
         c.init(Cipher.ENCRYPT_MODE, key);
         return Base64.getEncoder().encodeToString(c.doFinal(ccNumber.getBytes()));
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
    }
 
    @Override
    public String convertToEntityAttribute(String dbData) {
      Key key = new SecretKeySpec(KEY, "AES");
      try {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

}
