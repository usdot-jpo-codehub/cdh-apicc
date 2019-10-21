package gov.dot.its.cdh.dao;

import java.io.IOException;
import java.util.List;

import com.constantcontact.v2.contacts.Contact;

import gov.dot.its.cdh.model.CCContactRequest;


public interface ConstantContactDao {

	List<Contact> getContactsByEmail(String email) throws IOException;
	Contact createContact(CCContactRequest ccContactRequest) throws IOException;
	Contact registerContactToList(CCContactRequest ccContactRequest, Contact existingContact) throws IOException;
}
