package gov.dot.its.cdh.model;

public class ApiMessage {
	String message;
	
	public ApiMessage() {}
	public ApiMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}