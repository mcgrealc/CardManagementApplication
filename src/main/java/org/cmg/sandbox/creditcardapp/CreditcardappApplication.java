package org.cmg.sandbox.creditcardapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
public class CreditcardappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditcardappApplication.class, args);
	}
	
    @Value("${payment.gateway.url}")
    private String paymentGatewayUrl;
	
	@Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(paymentGatewayUrl));
		
		return restTemplate;
	}

}
