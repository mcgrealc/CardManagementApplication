package org.cmg.sandbox.creditcardapp.validators;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class CreditCardValidator implements CreditCardValidatorInterface {
	
	public boolean isValid(String creditCard) {
		
		return isValidCreditCardNumber(creditCard);
	}
	
	 private boolean isValidCreditCardNumber(String creditCard)
	    {
		 if(creditCard.length() < 12 || creditCard.length() >= 19) {
			 return false;
		 }
	        int[] cardIntArray=new int[creditCard.length()];
	 
	        for(int i=0;i<creditCard.length();i++)
	        {
	            char c= creditCard.charAt(i);
	            cardIntArray[i]=  Integer.parseInt(""+c);
	        }
	 
	        for(int i=cardIntArray.length-2;i>=0;i=i-2)
	        {
	            int num = cardIntArray[i];
	            num = num * 2;
	            if(num>9)
	            {
	                num = num%10 + num/10;
	            }
	            cardIntArray[i]=num;
	        }
	 
	        int sum = Arrays.stream(cardIntArray).sum();
	 	 
	        if(sum%10==0)
	        {
	            return true;
	        }
	 
	        return false;
	 
	    }

}
