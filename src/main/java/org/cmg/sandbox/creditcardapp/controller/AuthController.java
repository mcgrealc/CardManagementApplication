package org.cmg.sandbox.creditcardapp.controller;

import java.util.HashSet;
import java.util.Set;

import org.cmg.sandbox.creditcardapp.controller.request.LoginRequest;
import org.cmg.sandbox.creditcardapp.controller.request.SignupRequest;
import org.cmg.sandbox.creditcardapp.controller.response.JwtResponse;
import org.cmg.sandbox.creditcardapp.controller.response.MessageResponse;
import org.cmg.sandbox.creditcardapp.controller.response.ErrorMessageResponse;
import org.cmg.sandbox.creditcardapp.jwt.JwtUtils;
import org.cmg.sandbox.creditcardapp.model.Role;
import org.cmg.sandbox.creditcardapp.model.RoleEnum;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.repository.RoleRepository;
import org.cmg.sandbox.creditcardapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	  AuthenticationManager authenticationManager;

	  @Autowired
	  UserRepository userRepository;

	  @Autowired
	  RoleRepository roleRepository;

	  @Autowired
	  PasswordEncoder encoder;

	  @Autowired
	  JwtUtils jwtUtils;

	  @PostMapping("/signin")
		@Operation(
			      summary = "Signin to access credit card account.",
			      description = "Signin to access customer account for storage and usage of Credit Card Accounts. The response is a JWT or a descriptive error.")
		  @ApiResponses({
			    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json") }),
			    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
			  })
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);   

	    return ResponseEntity.ok(new JwtResponse(jwt));
	  }

	  @PostMapping("/signup")
		@Operation(
			      summary = "Signup for credit card account.",
			      description = "Create a customer account for storage and usage of Credit Card Accounts. The response is a status message or a descriptive error.")
		  @ApiResponses({
			    @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = "application/json") }),
			    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ErrorMessageResponse.class)) })
			  })
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    if (userRepository.existsByUserName(signUpRequest.getUserName())) {
	    	return new ResponseEntity<>(new ErrorMessageResponse(String.format("Error: Username is already taken!")), HttpStatus.BAD_REQUEST);
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	    	return new ResponseEntity<>(new ErrorMessageResponse(String.format("Error: Email is already taken!")), HttpStatus.BAD_REQUEST);
	    }

	    // Create new user's account
	    User user = new User(signUpRequest.getUserName(), 
	               signUpRequest.getEmail(),
	               encoder.encode(signUpRequest.getPassword()));

	    String strRole = signUpRequest.getRole();
	    Set<Role> roles = new HashSet<>();

	    try { 
	    if (strRole == null) {
	      Role userRole = roleRepository.findByRole(RoleEnum.ROLE_CUSTOMER)
	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	      roles.add(userRole);
	    } else {
	      
	        switch (strRole) {
	        case "admin":
	          Role adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(adminRole);

	          break;

	        default:
	          Role userRole = roleRepository.findByRole(RoleEnum.ROLE_CUSTOMER)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(userRole);
	        }
	      
	    }
	    }
	    catch(RuntimeException re) {
	    	logger.error("Error during user onboarding {}", re.getMessage());
	    	// Don't tell requester that role is missing to prevent them enumerating system information.
	    	return new ResponseEntity<>(new ErrorMessageResponse(String.format("Unable to create new user account at this time.")), HttpStatus.BAD_REQUEST);
	    }

	    user.setRoles(roles);
	    userRepository.save(user);

	    return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.CREATED);
	  }
	}
