package org.univ7.webapp.quiz.restcontroller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.entity.User;
import org.univ7.webapp.quiz.security.AuthorityManager;
import org.univ7.webapp.quiz.security.token.JsonTokenVo;
import org.univ7.webapp.quiz.service.UserService;
import org.univ7.webapp.quiz.util.AttributeSetter;
import org.univ7.webapp.quiz.util.JSONResponseUtil;
import org.univ7.webapp.quiz.vo.JsonMessageVo;

@Controller
public class LoginController {
	
	@Autowired
	private AttributeSetter as;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthorityManager authorityManager;
	
	@RequestMapping(value = UriResources.View.LOGIN, method=RequestMethod.GET)
	public String loginView(Model model) {
		as.setPageName(model, ServiceMessages.Login.LOGIN_PAGE_NAME);
		return "/view/login";
	}
	
	@RequestMapping(value = UriResources.View.LOGIN, method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Object login(@RequestBody User user, HttpSession session) {
		User findedUser = userService.findUser(user);
		if(findedUser != null && findedUser.login(user)) {
			authorityManager.setLoginStatusOnSessionAndContext(findedUser.getId(), session);
			JsonTokenVo token = userService.getToken(findedUser);
			return JSONResponseUtil.getJSONResponse(token, HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse(new JsonMessageVo(ServiceMessages.Login.CHECK_USER_ID_AND_PASSWORD_MESSAGE), HttpStatus.UNAUTHORIZED);
	}
}