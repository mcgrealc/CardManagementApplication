package org.cmg.sandbox.creditcardapp.controller.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorMessageResponse {
	@Schema(name="message", description="Message from system in the event of non successful request.", example="Something went wrong!")

	  private String message;

	  public ErrorMessageResponse(String message) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }

	@Override
	public int hashCode() {
		return Objects.hash(message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorMessageResponse other = (ErrorMessageResponse) obj;
		return Objects.equals(message, other.message);
	}

	@Override
	public String toString() {
		return "ErrorMessageResponse [message=" + message + "]";
	}
	  
	  
	}