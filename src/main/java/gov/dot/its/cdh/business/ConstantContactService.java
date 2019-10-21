package gov.dot.its.cdh.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.constantcontact.v2.contacts.Contact;

import gov.dot.its.cdh.model.ApiResponse;
import gov.dot.its.cdh.model.CCContactRequest;
import gov.dot.its.cdh.model.CCContactResponse;

public interface ConstantContactService {
	ApiResponse<List<Contact>> getContactsByEmail(HttpServletRequest request, String email);
	ApiResponse<CCContactResponse> createContact(HttpServletRequest request, CCContactRequest ccContactRequest);
}
