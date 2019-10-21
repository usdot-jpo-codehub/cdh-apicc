package gov.dot.its.cdh.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.constantcontact.v2.CCApi2;
import com.constantcontact.v2.ContactService;
import com.constantcontact.v2.Paged;
import com.constantcontact.v2.contacts.Contact;
import com.constantcontact.v2.contacts.ContactListMetaData;
import com.constantcontact.v2.contacts.EmailAddress;
import com.constantcontact.v2.contacts.OptInSource;

import gov.dot.its.cdh.model.CCContactRequest;
import retrofit2.Response;


@Repository
public class ConstantContactDaoImpl implements ConstantContactDao {
	@Value("${cdhapicc.apiKey}")
	private String apiKey;
	@Value("${cdhapicc.accessToken}")
	private String accessToken;

	private CCApi2 getCCApi() {
		return new CCApi2(this.apiKey, this.accessToken);
	}

	@Override
	public List<Contact> getContactsByEmail(String email) throws IOException {

		CCApi2 api = this.getCCApi();
		ContactService contactService = api.getContactService();
		Response<Paged<Contact>> contacts = contactService.getContactsByEmail(email).execute();

		return contacts.body().getResults();
	}

	@Override
	public Contact createContact(CCContactRequest ccContactRequest) throws IOException {
		CCApi2 api = this.getCCApi();
		
		List<ContactListMetaData> lists = new ArrayList<>();
		ContactListMetaData lst = new ContactListMetaData();
		lst.setId(ccContactRequest.getListId());
		lists.add(lst);
		
		List<EmailAddress> emailAddresses = new ArrayList<>();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmailAddress(ccContactRequest.getEmail());
		emailAddresses.add(emailAddress);
		
		ContactListMetaData[] listArray = lists.stream().toArray(ContactListMetaData[]::new);
		EmailAddress[] emailAddressArray = emailAddresses.stream().toArray(EmailAddress[]::new);
		
		Contact newContact = new Contact();
		newContact.setContactLists(listArray);
		newContact.setEmailAddresses(emailAddressArray);
		
		Response<Contact> response= api.getContactService().createContact(newContact, OptInSource.ACTION_BY_VISITOR).execute();
		
		return response.body();
	}

	@Override
	public Contact registerContactToList(CCContactRequest ccContactRequest, Contact existingContact) throws IOException {
		CCApi2 api = this.getCCApi();

		List<ContactListMetaData> listList = new ArrayList<>();
		for(ContactListMetaData list : existingContact.getContactLists()) {
			listList.add(list);
		}

		ContactListMetaData lst = new ContactListMetaData();
		lst.setId(ccContactRequest.getListId());
		listList.add(lst);

		ContactListMetaData[] listArray = listList.stream().toArray(ContactListMetaData[]::new);
		
		existingContact.setContactLists(listArray);

		Response<Contact> response= api.getContactService().updateContact(existingContact, existingContact.getId(), OptInSource.ACTION_BY_VISITOR).execute();
		
		return response.body();
	}


}
