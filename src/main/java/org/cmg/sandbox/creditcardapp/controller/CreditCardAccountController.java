package org.cmg.sandbox.creditcardapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cmg.sandbox.creditcardapp.controller.request.ChargeAccountRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditAccountRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditCardAccountCreditLimitUpdateRequest;
import org.cmg.sandbox.creditcardapp.controller.request.CreditCardAccountRequest;
import org.cmg.sandbox.creditcardapp.controller.response.CreditCardAccountResponse;
import org.cmg.sandbox.creditcardapp.controller.response.ErrorMessageResponse;
import org.cmg.sandbox.creditcardapp.controller.response.MessageResponse;
import org.cmg.sandbox.creditcardapp.exception.AccountLookupException;
import org.cmg.sandbox.creditcardapp.exception.AccountSaveException;
import org.cmg.sandbox.creditcardapp.exception.InvalidParameterException;
import org.cmg.sandbox.creditcardapp.exception.PaymentGatewayException;
import org.cmg.sandbox.creditcardapp.model.CreditCardAccount;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberChargeResponse;
import org.cmg.sandbox.creditcardapp.model.paymentgateway.CardNumberCreditResponse;
import org.cmg.sandbox.creditcardapp.repository.CreditCardAccountRepository;
import org.cmg.sandbox.creditcardapp.repository.UserRepository;
import org.cmg.sandbox.creditcardapp.service.CreditCardAccountService;
import org.cmg.sandbox.creditcardapp.service.PaymentGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CreditCardAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(CreditCardAccountController.class);
	
	@Autowired
	CreditCardAccountRepository creditCardAccountRepository;
	
	@Autowired
	CreditCardAccountService creditCardAccountService;
	
	@Autowired
	PaymentGatewayService paymentGatewayService;
	
	
	  @Autowired
	  UserRepository userRepository;
	
	@PostMapping("/credit-cards")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Create new credit card account",
		      description = "Create a new credit card account for the logged in user. The response is the newly created credit card account or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = CreditCardAccountResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "400", description = "Unable to create Credit Card Account - reason [reason if available]", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> createNewCreditCardAccount(@Valid @RequestBody CreditCardAccountRequest creditCardAccountRequest){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName) );
		
		final CreditCardAccount newAccount = new CreditCardAccount(lookupUser, creditCardAccountRequest.getCardNumber(), creditCardAccountRequest.getCardExpiry(), creditCardAccountRequest.getCardHolderName(), creditCardAccountRequest.getCreditLimit() );
		
		CreditCardAccount savedAccount = creditCardAccountService.saveCreditCardAccount(newAccount);
		
		if(savedAccount != null) {
			return new ResponseEntity<>(new MessageResponse(String.format("Created Credit Card Account with Card ending %s", savedAccount.getMaskedCardLastFour())), HttpStatus.CREATED);
		}
		
		} catch( InvalidParameterException | AccountSaveException | UsernameNotFoundException ex) {
			logger.debug("Unable to create Credit Card Account [{}]", ex.getMessage());
			return new ResponseEntity<>(new ErrorMessageResponse(String.format("Unable to create Credit Card Account - reason [%s]", ex.getMessage())), HttpStatus.BAD_REQUEST);
		}	
				
		return new ResponseEntity<>(new ErrorMessageResponse("Unable to create Credit Card Account"), HttpStatus.BAD_REQUEST);
	
	}
	
	@GetMapping("/credit-cards")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Retrieve all credit card accounts.",
		      description = "Retrieve all credit card accounts belonging to logged in user. The response is a list of credit card accounts or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = CreditCardAccountResponse.class)), mediaType = "application/json") }),
		  })
	public ResponseEntity<?> findAllCreditCardAccounts(){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName) );
		
		final List<CreditCardAccount> accounts = creditCardAccountService.fetchAllCreditCardAccounts(lookupUser.getId());
		
		final List<CreditCardAccountResponse> responseList = accounts.stream().map(account -> new CreditCardAccountResponse(account.getId(), account.getMaskedCardLastFour(), account.getCardHolderName(), account.getCreditLimit())).collect(Collectors.toList());
		return new ResponseEntity<>(responseList, HttpStatus.OK);
		} catch( UsernameNotFoundException ex) {
			logger.debug("User [{}] does not have authorization to retrieve Credit Card Accounts.", ex.getMessage());
			return new ResponseEntity<>(new ErrorMessageResponse("You are not authorized to access this resource"), HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@GetMapping("/credit-cards/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Retrieve credit card account.",
		      description = "Retrieve specified credit card account belonging to logged in user. The response is a credit card account or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CreditCardAccountResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> findCreditCardAccountById(@Valid @PathVariable @Parameter(description = "Credit Card Account Identifier", required = true, example = "1") Long id){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName) );
		
	    CreditCardAccount account = new CreditCardAccount();
		
			account = creditCardAccountService.fetchCreditCardAccountById(id).filter(a -> a.getUser().equals(lookupUser)).orElseThrow(() -> new ResourceAccessException(userName));
			final CreditCardAccountResponse response = new CreditCardAccountResponse(account.getId(), account.getMaskedCardLastFour(), account.getCardHolderName(), account.getCreditLimit());
			return new ResponseEntity<>(response, HttpStatus.OK);	
		} catch( UsernameNotFoundException | ResourceAccessException ex) {
			logger.debug("User [{}] does not have authorization to retrieve Credit Card Accounts.", ex.getMessage());
			return new ResponseEntity<>(new ErrorMessageResponse("You are not authorized to access this resource"), HttpStatus.UNAUTHORIZED);
		}
			
	}
	
	@PutMapping("/credit-cards/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Update credit card account limit.",
		      description = "Update the credit limit for the specified credit card account belonging to logged in user. The response is a status message or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> updateCreditCardAccountById(@PathVariable @Parameter(description = "Credit Card Account Identifier", required = true, example = "1") Long id, @Valid @RequestBody CreditCardAccountCreditLimitUpdateRequest creditCardAccountCreditLimitUpdateRequest){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName) );
		
		final CreditCardAccount account = creditCardAccountService.fetchCreditCardAccountById(id).filter(a -> a.getUser().equals(lookupUser)).orElseThrow(() -> new ResourceAccessException("You are not authorized to access this resource"));

			Optional<CreditCardAccount> accountUpdate = creditCardAccountService.updateCreditCardAccountCreditLimit(account.getId(), creditCardAccountCreditLimitUpdateRequest.getCreditLimit());
			
			if(accountUpdate.isPresent()) {
				return new ResponseEntity<>(new MessageResponse("Updated Credit Card Account successfully"), HttpStatus.NO_CONTENT);
			}
		} catch( ResourceAccessException | AccountLookupException | UsernameNotFoundException ex) {
			return new ResponseEntity<>(new ErrorMessageResponse("You are not authorized to access this resource"), HttpStatus.UNAUTHORIZED);
		}
					
		return new ResponseEntity<>(new ErrorMessageResponse("Unable to update Credit Card Account Credit Limit"), HttpStatus.NOT_MODIFIED);
			
	}
	
	@DeleteMapping("/credit-cards/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Delete credit card account.",
		      description = "Delete the specified credit card account belonging to logged in user. The response is a status message or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> deleteCreditCardAccountById(@PathVariable @Parameter(description = "Credit Card Account Identifier", required = true, example = "1") Long id){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName) );
		
		final Optional<CreditCardAccount> account = creditCardAccountService.fetchCreditCardAccountById(id).filter(a -> a.getUser().equals(lookupUser));
		
		boolean isDeleted = false;
		
		if(account.isPresent()) {
			isDeleted = creditCardAccountService.deleteCreditCardAccount(id);
		}
		
		if(isDeleted) {
			return new ResponseEntity<>(new MessageResponse("Deleted Credit Card Account successfully"), HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(new ErrorMessageResponse("Unable to delete Credit Card Account"), HttpStatus.BAD_REQUEST);
			
	}
	
	@PostMapping("/credit-cards/{cardNumber}/charge")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Charge credit card account.",
		      description = "Charge the specified credit card account belonging to logged in user, with the specified amount. The response is a status message or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> chargeCreditCardAccount(@Valid @PathVariable @Parameter(description = "Credit Card Number", required = true, example = "4444333322221111") String cardNumber,  @RequestBody ChargeAccountRequest chargeAccountRequest){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));

		creditCardAccountService.fetchCreditCardAccountByCardNumber(cardNumber).filter(a -> a.getUser().equals(lookupUser)).orElseThrow(() -> new ResourceAccessException("You are not authorized to access this resource"));

		CardNumberChargeResponse cardNumberChargeResponse = paymentGatewayService.chargeCreditCardAccount(cardNumber, chargeAccountRequest.getAmount());
				
		if(cardNumberChargeResponse.getPaymentStatus().equals("Success")) {
			return new ResponseEntity<>(new MessageResponse(String.format("Credit Card Account with card ending [%s] charged with amount [%.2f]", cardNumberChargeResponse.getCardEnding(), cardNumberChargeResponse.getAmountCharged())), HttpStatus.OK);
		}else
			{				
			return new ResponseEntity<>(new ErrorMessageResponse(String.format("Unable to charge Credit Card Account - reason [%s]", cardNumberChargeResponse.getPaymentStatusReason())), HttpStatus.BAD_REQUEST);
			} 
		} catch(ResourceAccessException | UsernameNotFoundException | PaymentGatewayException ex ) {
			logger.debug("User [{}] does not have authorization to retrieve Credit Card Accounts.", ex.getMessage());
			return new ResponseEntity<>(new ErrorMessageResponse(String.format("You are not authorized to use this credit card")), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/credit-cards/{cardNumber}/credit")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(
		      summary = "Credit credit card account.",
		      description = "Credit the specified credit card account belonging to logged in user, with the specified amount. The response is a status message or a descriptive error.")
	  @ApiResponses({
		    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
		  })
	public ResponseEntity<?> creditCreditCardAccount(@Valid @PathVariable @Parameter(description = "Credit Card Number", required = true, example = "4444333322221111") String cardNumber,  @RequestBody CreditAccountRequest creditAccountRequest){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
		String userName = userDetails.getUsername();
		
		try {
		User lookupUser = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));

		creditCardAccountService.fetchCreditCardAccountByCardNumber(cardNumber).filter(a -> a.getUser().equals(lookupUser)).orElseThrow(() -> new ResourceAccessException("You are not authorized to access this resource"));

		CardNumberCreditResponse cardNumberCreditResponse = paymentGatewayService.creditCreditCardAccount(cardNumber, creditAccountRequest.getAmount());
				
		if(cardNumberCreditResponse.getPaymentStatus().equals("Success")) {
			return new ResponseEntity<>(new MessageResponse(String.format("Credit Card Account with card ending [%s] credited with amount [%.2f]", cardNumberCreditResponse.getCardEnding(), cardNumberCreditResponse.getAmountCredited())), HttpStatus.OK);
		}
		else {					
			return new ResponseEntity<>(new ErrorMessageResponse(String.format("Unable to credit Credit Card Account - reason [%s]", cardNumberCreditResponse.getPaymentStatusReason())), HttpStatus.BAD_REQUEST);
		}
		} catch(ResourceAccessException | UsernameNotFoundException | PaymentGatewayException ex ) {
			logger.debug("User [{}] does not have authorization to retrieve Credit Card Accounts.", ex.getMessage());
			return new ResponseEntity<>(new ErrorMessageResponse(String.format("You are not authorized to use this credit card")), HttpStatus.BAD_REQUEST);
		}
		
	}
	

}
