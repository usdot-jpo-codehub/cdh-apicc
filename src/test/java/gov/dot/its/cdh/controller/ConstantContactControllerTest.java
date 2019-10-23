package gov.dot.its.cdh.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.cdh.business.ConstantContactService;
import gov.dot.its.cdh.model.ApiResponse;
import gov.dot.its.cdh.model.CCContactRequest;
import gov.dot.its.cdh.model.CCContactResponse;

@RunWith(SpringRunner.class)
@WebMvcTest(ConstantContactController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3003, uriScheme="http")
public class ConstantContactControllerTest {

	private static final String TEST_ID = "1234567890";
	private static final String TEST_LISTID = "0987654321";
	private static final String TEST_EMAIL = "test@email.com";

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private ConstantContactService constantContactService;

	@Test
	public void testCreateContact() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		CCContactRequest ccContactRequest= new CCContactRequest();
		ccContactRequest.setEmail(TEST_EMAIL);
		ccContactRequest.setListId(TEST_LISTID);
		String requestJson = new ObjectMapper().writeValueAsString(ccContactRequest);

		ApiResponse<CCContactResponse> apiResponse = new ApiResponse<>();
		CCContactResponse ccContactResponse = new CCContactResponse();
		ccContactResponse.setEmail(TEST_EMAIL);
		ccContactResponse.setId(TEST_ID);
		ccContactResponse.setListId(TEST_LISTID);
		apiResponse.setResponse(HttpStatus.OK, ccContactResponse, null, null, request);

		when(constantContactService.createContact(any(HttpServletRequest.class), any(CCContactRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mvc.perform(
				post(String.format("%s/v1/contacts",env.getProperty("server.servlet.context-path")))
				.contextPath(String.format("%s", env.getProperty("server.servlet.context-path")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)
				)
			.andExpect(status().isOk())
			.andDo(document("api/v1/contact",
					Preprocessors.preprocessRequest(
							Preprocessors.prettyPrint(),
							Preprocessors.removeHeaders("Host", "Content-Length")
							),
					Preprocessors.preprocessResponse(
							Preprocessors.prettyPrint(),
							Preprocessors.removeHeaders("Host", "Content-Length")
							)
					));

		MvcResult result = resultActions.andReturn();
		String contentStr = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CCContactResponse>> valueType = new TypeReference<ApiResponse<CCContactResponse>>() {}; 
		ApiResponse<CCContactResponse> responseApi = objectMapper.readValue(contentStr, valueType);
		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertEquals(TEST_EMAIL, responseApi.getResult().getEmail());
		assertEquals(TEST_ID, responseApi.getResult().getId());
		assertEquals(TEST_LISTID, responseApi.getResult().getListId());

	}

}
