package gov.dot.its.cdh.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.constantcontact.v2.contacts.Contact;
import com.constantcontact.v2.contacts.ContactListMetaData;

import gov.dot.its.cdh.dao.ConstantContactDao;
import gov.dot.its.cdh.model.ApiError;
import gov.dot.its.cdh.model.ApiMessage;
import gov.dot.its.cdh.model.ApiResponse;
import gov.dot.its.cdh.model.CCContactRequest;
import gov.dot.its.cdh.model.CCContactResponse;

@Service
public class ConstantContactServiceImpl implements ConstantContactService {
	
	@Autowired
	ConstantContactDao constantContactDao;

	
	@Override
	public ApiResponse<List<Contact>> getContactsByEmail(HttpServletRequest request, String email) {
		ApiResponse<List<Contact>> apiResponse = new ApiResponse<>();
		
		try {
			
			List<Contact> contacts = constantContactDao.getContactsByEmail(email);
			if( contacts.isEmpty() ) {
				apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
				return apiResponse;
			} else {
				apiResponse.setResponse(HttpStatus.OK,  contacts, null, null, request);
				return apiResponse;
			}
				
		} catch(IOException e) {
			List<ApiError> errors = new ArrayList<>();
			errors.add(new ApiError(e.getMessage()));
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);
			return apiResponse;
		}
	}

	@Override
	public ApiResponse<CCContactResponse> createContact(HttpServletRequest request, CCContactRequest ccContactRequest) {
		ApiResponse<CCContactResponse> apiResponse = new ApiResponse<>();
		List<ApiMessage> messages = new ArrayList<>(); 
		
		if (ccContactRequest == null || StringUtils.isEmpty(ccContactRequest.getEmail()) || StringUtils.isEmpty(ccContactRequest.getListId())) {
			messages.add(new ApiMessage("The request body is empty or does not containt the required information."));
			apiResponse.setResponse(HttpStatus.BAD_REQUEST, null, messages, null, request);
			return apiResponse;
		}

		try {

			List<Contact> contacts = constantContactDao.getContactsByEmail(ccContactRequest.getEmail());

			CCContactResponse contactResponse = null;
			HttpStatus httpStatus = null;
			if (contacts == null || contacts.isEmpty()) {
				Contact contact = constantContactDao.createContact(ccContactRequest);
				contactResponse = getContactResponse(contact.getId(), ccContactRequest.getEmail(), ccContactRequest.getListId());
				httpStatus = HttpStatus.CREATED;
			} else {
				Contact existingContact = contacts.get(0);
				if (isContactRegistered(existingContact.getContactLists(), ccContactRequest.getListId())) {
					messages.add(new ApiMessage("Contact was already registered"));
					httpStatus = HttpStatus.OK;
				} else {
					Contact contact = constantContactDao.registerContactToList(ccContactRequest, existingContact);
					contactResponse = getContactResponse(contact.getId(), ccContactRequest.getEmail(), ccContactRequest.getListId());
					messages.add(new ApiMessage("Contact registered"));
					httpStatus = HttpStatus.OK;
				}
			}

			apiResponse.setResponse(httpStatus, contactResponse, messages, null, request);
			return apiResponse;

		} catch(IOException e) {

			List<ApiError> errors = new ArrayList<>();
			errors.add(new ApiError(e.getMessage()));
			apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);
			return apiResponse;

		}
	}

	private CCContactResponse getContactResponse(String id, String email, String listId) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		CCContactResponse contactResponse = new CCContactResponse();
		contactResponse.setId(id);
		contactResponse.setEmail(email);
		contactResponse.setListId(listId);

		return contactResponse;
	}

	private boolean isContactRegistered(ContactListMetaData[] contactLists, String listId) {
		for(ContactListMetaData list : contactLists) {
			if(list.getId().compareTo(listId) == 0) {
				return true;
			}
		}
		return false;
	}
	
	

}
