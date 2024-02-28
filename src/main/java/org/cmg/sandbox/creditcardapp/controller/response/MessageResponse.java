package org.cmg.sandbox.creditcardapp.controller.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public class MessageResponse {
	@Schema(name="message", description="Message from system in the event of successful request when no content is returned.", example="Request successfully completed!")

	  private String message;

	  public MessageResponse(String message) {
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
		MessageResponse other = (MessageResponse) obj;
		return Objects.equals(message, other.message);
	}

	@Override
	public String toString() {
		return "MessageResponse [message=" + message + "]";
	}
	  
	  
	}