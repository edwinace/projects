package org.univ7.webapp.quiz.util;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class AttributeSetter {

	public void setPageName(Model model, String title) {
		setAttribute(model, "page-name", title);
	}
	public void setAttribute(Model model, String key, Object value) {
		model.addAttribute(key, value);
	}
}
