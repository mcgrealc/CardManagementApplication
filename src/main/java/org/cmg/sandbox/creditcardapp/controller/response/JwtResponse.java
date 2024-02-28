package org.cmg.sandbox.creditcardapp.controller.response;


import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public class JwtResponse {
	 @Schema(name="token", description="JSON Web Token for authorization to access this services APIs")
	  private String token;
	 @Schema(name="token", description="JSON Web Token Type (Bearer)")
	  private String type = "Bearer";

	  public JwtResponse(String accessToken) {
	    this.token = accessToken;
	  }

	  public String getAccessToken() {
	    return token; 
	  }

	  public void setAccessToken(String accessToken) {
	    this.token = accessToken;
	  }

	  public String getTokenType() {
	    return type;
	  }

	  public void setTokenType(String tokenType) {
	    this.type = tokenType;
	  }

	@Override
	public int hashCode() {
		return Objects.hash(token, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JwtResponse other = (JwtResponse) obj;
		return Objects.equals(token, other.token) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + "]";
	}
	  
	  

	}
