package gov.dot.its.cdh.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.cdh.business.ConstantContactService;
import gov.dot.its.cdh.model.ApiResponse;
import gov.dot.its.cdh.model.CCContactRequest;
import gov.dot.its.cdh.model.CCContactResponse;

@RestController
public class ConstantContactController {

	@Autowired
	ConstantContactService constantContactService;

	@PostMapping(value= {"/v1/contacts"}, consumes = {"application/json"}, produces= {"application/json"})
	public ResponseEntity<ApiResponse<CCContactResponse>> createContact(HttpServletRequest request, @RequestBody CCContactRequest ccContactRequest) {

		ApiResponse<CCContactResponse> apiResponse = constantContactService.createContact(request, ccContactRequest);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}

