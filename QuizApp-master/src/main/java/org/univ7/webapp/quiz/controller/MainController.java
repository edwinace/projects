package org.univ7.webapp.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.util.AttributeSetter;

@Controller
public class MainController {
	
	@Autowired
	private AttributeSetter as;

	@RequestMapping(value = UriResources.Main.LAYOUT, method = RequestMethod.GET)
	public String layout(Model model) {
		as.setAttribute(model, "organization-name", ServiceMessages.Program.ORGANIZATION_NAME);
		as.setAttribute(model, "program-name", ServiceMessages.Program.PROGRAM_NAME);
		return "/default";
	}
}