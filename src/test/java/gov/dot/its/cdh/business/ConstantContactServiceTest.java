package gov.dot.its.cdh.business;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.constantcontact.v2.contacts.Contact;
import com.constantcontact.v2.contacts.ContactListMetaData;
import com.constantcontact.v2.contacts.EmailAddress;

import gov.dot.its.cdh.dao.ConstantContactDao;
import gov.dot.its.cdh.model.ApiResponse;
import gov.dot.its.cdh.model.CCContactRequest;
import gov.dot.its.cdh.model.CCContactResponse;

@RunWith(SpringRunner.class)
public class ConstantContactServiceTest {

	private static final String TEST_LISTID = "1234567890";
	private static final String TEST_EMAIL = "test@email.com";
	private static final String TEST_ID = "0987654321";

	@InjectMocks
	ConstantContactServiceImpl constantContactService;

	@Mock
	ConstantContactDao constantContactDao;

	@Before
	public void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateContactContactRequestNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = null;

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testCreateContactContactrequestEmailNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testCreateContactContactrequestEmailEmpty() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setEmail("");

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testCreateContactContactrequestListIdNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getMessages().isEmpty());
	}
	
	@Test
	public void testCreateContactContactRequestListIdEmpty() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId("");

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testCreateContactContactRequestDaoGetContactsByEmailException() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId(TEST_LISTID);
		ccContactRequest.setEmail(TEST_EMAIL);

		when(constantContactDao.getContactsByEmail(any(String.class))).thenThrow(new IOException("Test IOException"));

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertTrue(!apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testCreateContactContactRequestDaoGetContactsByEmailNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId(TEST_LISTID);
		ccContactRequest.setEmail(TEST_EMAIL);

		List<Contact> contacts = null;

		Contact contact = this.prepareTestContact(TEST_ID, TEST_EMAIL, TEST_LISTID);

		when(constantContactDao.getContactsByEmail(any(String.class))).thenReturn(contacts);
		when(constantContactDao.createContact(any(CCContactRequest.class))).thenReturn(contact);

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.CREATED.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertEquals(apiResponse.getResult().getId(), TEST_ID);
		assertEquals(apiResponse.getResult().getListId(), TEST_LISTID);
		assertEquals(apiResponse.getResult().getEmail(), TEST_EMAIL);
	}

	@Test
	public void testCreateContactContactRequestDaoGetContactsByEmailEmpty() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId(TEST_LISTID);
		ccContactRequest.setEmail(TEST_EMAIL);

		List<Contact> contacts = new ArrayList<>();

		Contact contact = this.prepareTestContact(TEST_ID, TEST_EMAIL, TEST_LISTID);

		when(constantContactDao.getContactsByEmail(any(String.class))).thenReturn(contacts);
		when(constantContactDao.createContact(any(CCContactRequest.class))).thenReturn(contact);

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.CREATED.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertEquals(apiResponse.getResult().getId(), TEST_ID);
		assertEquals(apiResponse.getResult().getListId(), TEST_LISTID);
		assertEquals(apiResponse.getResult().getEmail(), TEST_EMAIL);
	}

	@Test
	public void testCreateContactContactRequestExistingContactAlreadyRegistered() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId(TEST_LISTID);
		ccContactRequest.setEmail(TEST_EMAIL);

		Contact contact = this.prepareTestContact(TEST_ID, TEST_EMAIL, TEST_LISTID);
		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);

		when(constantContactDao.getContactsByEmail(any(String.class))).thenReturn(contacts);

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() == null);
		assertTrue(!apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testCreateContactContactRequestExistingContactNotRegister() throws IOException {
		String testListIdNew = "24680";
		MockHttpServletRequest request = new MockHttpServletRequest();
		CCContactRequest ccContactRequest = new CCContactRequest();
		ccContactRequest.setListId(testListIdNew);
		ccContactRequest.setEmail(TEST_EMAIL);

		Contact contact = this.prepareTestContact(TEST_ID, TEST_EMAIL, TEST_LISTID);
		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);

		when(constantContactDao.getContactsByEmail(any(String.class))).thenReturn(contacts);
		when(constantContactDao.registerContactToList(any(CCContactRequest.class),any(Contact.class))).thenReturn(contact);

		ApiResponse<CCContactResponse> apiResponse  = constantContactService.createContact(request, ccContactRequest);

		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertTrue(apiResponse.getResult() != null);
		assertTrue(!apiResponse.getMessages().isEmpty());
		assertEquals(apiResponse.getResult().getId(), TEST_ID);
		assertEquals(apiResponse.getResult().getListId(), testListIdNew);
		assertEquals(apiResponse.getResult().getEmail(), TEST_EMAIL);
	}

	private Contact prepareTestContact(String id, String email, String listId) {
		List<ContactListMetaData> lists = new ArrayList<>();
		ContactListMetaData lst = new ContactListMetaData();
		lst.setId(listId);
		lists.add(lst);

		List<EmailAddress> emailAddresses = new ArrayList<>();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmailAddress(email);
		emailAddresses.add(emailAddress);

		ContactListMetaData[] listArray = lists.stream().toArray(ContactListMetaData[]::new);
		EmailAddress[] emailAddressArray = emailAddresses.stream().toArray(EmailAddress[]::new);

		Contact contact = new Contact();
		contact.setId(id);
		contact.setContactLists(listArray);
		contact.setEmailAddresses(emailAddressArray);

		return contact;
	}

}
