package gov.dot.its.cdh.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiResponse<T> {
	private Timestamp timestamp;
	private String status;
	private int code;
	private String path;
	private String verb;
	private String traceid;
	private T result;
	@JsonInclude(Include.NON_NULL)
	private List<ApiError> errors;
	@JsonInclude(Include.NON_NULL)
	private List<ApiMessage> messages;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getTraceid() {
		return traceid;
	}
	public void setTraceid(String traceid) {
		this.traceid = traceid;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public List<ApiError> getErrors() {
		return errors;
	}
	public void setErrors(List<ApiError> errors) {
		this.errors = errors;
	}
	public List<ApiMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<ApiMessage> messages) {
		this.messages = messages;
	}
	public void setResponse(HttpStatus httpStatus, T data, List<ApiMessage> messages, List<ApiError> errors, HttpServletRequest request) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		this.setTimestamp(ts);
		this.setCode(httpStatus.value());
		this.setStatus(httpStatus.name());
		this.setResult(data);
		this.setVerb(request.getMethod());
		this.setPath(buildRequestPath(request));
		this.setMessages(messages == null || messages.isEmpty() ? null : messages);
		this.setErrors(errors == null || errors.isEmpty() ? null : errors);
		
		SimpleDateFormat sdfTraceId = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sdfTraceId.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.setTraceid(sdfTraceId.format(ts));
	}
	private String buildRequestPath(HttpServletRequest request) {
		String requestUrl = request.getRequestURL().toString();
		requestUrl += request.getQueryString() != null ? "?"+request.getQueryString(): "";
		return requestUrl;
	}
}
