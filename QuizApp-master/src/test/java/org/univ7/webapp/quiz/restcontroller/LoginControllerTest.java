package org.univ7.webapp.quiz.restcontroller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.univ7.webapp.quiz.QuizAppApplication;
import org.univ7.webapp.quiz.constant.ContentTypes;
import org.univ7.webapp.quiz.constant.JsonPaths;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.entity.User;
import org.univ7.webapp.quiz.repository.UserRepository;
import org.univ7.webapp.quiz.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuizAppApplication.class)
@WebAppConfiguration
@Transactional
public class LoginControllerTest {
	private final String JSON_USER_RESPONSE_TEMPLATE = "{\"userId\":\"%s\",\"password\":\"%s\"}";

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void getLoginViewTest() throws Exception {
		mockMvc.perform(get(UriResources.View.LOGIN)).andExpect(status().is2xxSuccessful()).andExpect(status().isOk())
				.andExpect(content().contentType(ContentTypes.TEXT_HTML_CHARSET_UTF_8))
				.andExpect(content().string(containsString(ServiceMessages.Login.LOGIN_PAGE_NAME)));
	}

	@Test
	public void loginFailTest() throws Exception {
		insertTestUser();

		mockMvc.perform(post(UriResources.View.LOGIN).contentType(MediaType.APPLICATION_JSON).content(
				String.format(JSON_USER_RESPONSE_TEMPLATE, "user", "password").getBytes()))
				.andExpect(status().is4xxClientError())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath(JsonPaths.MESSAGE,
						equalTo(ServiceMessages.Login.CHECK_USER_ID_AND_PASSWORD_MESSAGE)));
	}

	private void insertTestUser() {
		userRepository.save(new User(1L, "userId", "password"));
	}
}
