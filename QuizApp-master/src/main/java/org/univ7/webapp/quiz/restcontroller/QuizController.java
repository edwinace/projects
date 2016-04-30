package org.univ7.webapp.quiz.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.util.AttributeSetter;
import org.univ7.webapp.quiz.util.JSONResponseUtil;
import org.univ7.webapp.quiz.vo.JsonMessageVo;

@Controller
public class QuizController {
	@Autowired
	private AttributeSetter as;
	
	@RequestMapping(value = UriResources.PrivateApi.TODAY_QUIZ, method = RequestMethod.GET)
	public String todayQuizView(Model model) {
		as.setPageName(model, ServiceMessages.Quiz.TODAY_QUIZ_PAGE_NAME);
		return "/view/quiz/todayQuiz";
	}
	
	@RequestMapping(value = UriResources.PrivateApi.TODAY_QUIZ, method = RequestMethod.POST)
	public ResponseEntity<Object> todayQuizCheckAnswer(String answer) {
		if("Apple".equals(answer)) {
			return successResponse("true");
		}
		return successResponse("false");
	}
	
	private ResponseEntity<Object> successResponse(String message) {
		JsonMessageVo vo = new JsonMessageVo(message);
		return JSONResponseUtil.getJSONResponse(vo, HttpStatus.OK);
	}
	
	private ResponseEntity<Object> successResponse() {
		return successResponse("");
	}
}