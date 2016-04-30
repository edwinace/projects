package org.univ7.webapp.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.util.AttributeSetter;

@Controller
public class ViewController {

	@Autowired
	private AttributeSetter as;

//	@Secured("ROLE_USER")
	@RequestMapping(value = UriResources.View.MAIN_VIEW, method = RequestMethod.GET)
	public String mainView(Model model) {
		as.setPageName(model, ServiceMessages.Main.MAIN_PAGE_NAME);
		return "/view/main";
	}
}