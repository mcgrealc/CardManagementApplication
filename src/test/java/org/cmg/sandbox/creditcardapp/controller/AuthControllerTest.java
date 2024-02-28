package org.cmg.sandbox.creditcardapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.cmg.sandbox.creditcardapp.controller.request.LoginRequest;
import org.cmg.sandbox.creditcardapp.controller.request.SignupRequest;
import org.cmg.sandbox.creditcardapp.jwt.JwtUtils;
import org.cmg.sandbox.creditcardapp.model.Role;
import org.cmg.sandbox.creditcardapp.model.RoleEnum;
import org.cmg.sandbox.creditcardapp.model.User;
import org.cmg.sandbox.creditcardapp.repository.RoleRepository;
import org.cmg.sandbox.creditcardapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {


	private static final String TEST_USERNAME_VALID = "User Name";

	private static final String TEST_EMAIL = "user@gmail.com";

	private static final String TEST_PASSWORD = "Pa$$w0rd123!";

	private static final String TEST_ROLE = "customer";
	
	private static final String MOCK_JWT = "guywdgdswciuhwehfewudijciuhehfehfecqwqewfd";

	@Autowired
    private MockMvc             mockMvc;
       
    @Autowired
    @MockBean
    UserRepository mockUserRepository;
    
	  @Autowired
	  @MockBean
	  RoleRepository mockRoleRepository;
	  
	  @Autowired
	  @MockBean
	  PasswordEncoder mockEncoder;
	  
	  @Autowired
	  @MockBean
	  JwtUtils mockJwtUtils;
	  
	  @Autowired
	  @MockBean
	  AuthenticationManager mockAuthenticationManager;
	  
	    @Autowired
	    @MockBean
	    RestTemplate mockRestTemplate;
	    
    User testUser;
    
    @Mock
    Authentication mockAuth;
    
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenAuthController_whenPostToSignup_thenNewUserResponseReturned() throws Exception
    {
    	
    	SignupRequest signupRequest = new SignupRequest(TEST_USERNAME_VALID, TEST_EMAIL, TEST_ROLE, TEST_PASSWORD);
    	
    	when(mockUserRepository.existsByUserName(TEST_USERNAME_VALID)).thenReturn(false);
    	
    	when(mockUserRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
    	
    	Role testRole = new Role(RoleEnum.ROLE_CUSTOMER);
    	
    	when(mockRoleRepository.findByRole(RoleEnum.ROLE_CUSTOMER)).thenReturn(Optional.of(testRole));
    	
    	final MvcResult result = mockMvc.perform(post("/api/auth/signup").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(signupRequest)))
                                        .andExpect(status().isCreated()).andReturn();
    	
        assertEquals("{\"message\":\"User registered successfully!\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenAuthController_whenPostToSignupExistingUsername_thenErrorResponseReturned() throws Exception
    {
    	
    	SignupRequest signupRequest = new SignupRequest(TEST_USERNAME_VALID, TEST_EMAIL, TEST_ROLE, TEST_PASSWORD);
    	
    	when(mockUserRepository.existsByUserName(TEST_USERNAME_VALID)).thenReturn(true);

    	
    	final MvcResult result = mockMvc.perform(post("/api/auth/signup").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(signupRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Error: Username is already taken!\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenAuthController_whenPostToSignupExistingEmail_thenErrorResponseReturned() throws Exception
    {
    	
    	SignupRequest signupRequest = new SignupRequest(TEST_USERNAME_VALID, TEST_EMAIL, TEST_ROLE, TEST_PASSWORD);
    	
    	when(mockUserRepository.existsByUserName(TEST_USERNAME_VALID)).thenReturn(false);
    	
    	when(mockUserRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

    	
    	final MvcResult result = mockMvc.perform(post("/api/auth/signup").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(signupRequest)))
                                        .andExpect(status().isBadRequest()).andReturn();
    	
        assertEquals("{\"message\":\"Error: Email is already taken!\"}", result.getResponse().getContentAsString());                                               	    		  
    }
    
    @Test
    @WithMockUser(username=TEST_USERNAME_VALID,roles={"CUSTOMER"})
    public void givenAuthController_whenPostTo
    Signin_thenJWTResponseReturned() throws Exception
    {
    	
    	LoginRequest loginRequest = new LoginRequest();
    	
    	loginRequest.setUserName(TEST_USERNAME_VALID);
    	
    	loginRequest.setPassword(TEST_PASSWORD);
    	
    	when(mockAuthenticationManager.authenticate(
    	        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()))).thenReturn(mockAuth);

    	
    	when(mockJwtUtils.generateJwtToken(mockAuth)).thenReturn(MOCK_JWT);
    	
    	final MvcResult result = mockMvc.perform(post("/api/auth/signin").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(loginRequest)))
                                        .andExpect(status().isOk()).andReturn();
    	
        assertTrue(result.getResponse().getContentAsString().contains(MOCK_JWT));                                               	    		  
    }
    

    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
