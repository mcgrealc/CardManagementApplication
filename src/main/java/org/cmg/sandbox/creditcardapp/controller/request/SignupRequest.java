package org.cmg.sandbox.creditcardapp.controller.request;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  @Schema(name="userName", description="Unique user name to access customers credit card accounts", example="testU$er123")
  private String userName;

  @NotBlank
  @Size(max = 50)
  @Email
  @Schema(name="email", description="Customers email address", example="testuser@gmail.com")
  private String email;

  @Schema(name="role", description="Role of user - normally customer", example="customer")
  private String role;

  @NotBlank
  @Size(min = 6, max = 40)
  @Schema(name="password", description="Password to secure access", example="secret123!")
  private String password;
  
  
  public SignupRequest() {

}

public SignupRequest(@NotBlank @Size(min = 3, max = 20) String userName, @NotBlank @Size(max = 50) @Email String email,
		String role, @NotBlank @Size(min = 6, max = 40) String password) {
	super();
	this.userName = userName;
	this.email = email;
	this.role = role;
	this.password = password;
}

public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

@Override
public int hashCode() {
	return Objects.hash(email, password, role, userName);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	SignupRequest other = (SignupRequest) obj;
	return Objects.equals(email, other.email) && Objects.equals(password, other.password)
			&& Objects.equals(role, other.role) && Objects.equals(userName, other.userName);
}

@Override
public String toString() {
	return "SignupRequest [userName=" + userName + ", email=" + email + ", role=" + role + ", password=" + password
			+ "]";
}
  
  
  
}
