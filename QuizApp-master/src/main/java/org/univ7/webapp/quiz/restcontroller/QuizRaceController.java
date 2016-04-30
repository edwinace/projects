package org.univ7.webapp.quiz.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.repository.QuestionRepository;
import org.univ7.webapp.quiz.util.AttributeSetter;

@Controller
public class QuizRaceController {
	@Autowired
	private AttributeSetter as;
	@Autowired
	private QuestionRepository questionRepository;

	@RequestMapping(value = UriResources.PrivateApi.QUIZ_RACE, method = RequestMethod.GET)
	public String quizRaceView(Model model) {
		as.setPageName(model, ServiceMessages.QuizRace.QUIZ_RACE_PAGE_NAME);
		return "/view/quizRace/main";
	}

	@RequestMapping(value = UriResources.PrivateApi.QUIZ_RACE + "/{questionNum}", method = RequestMethod.GET)
	public String quizRaceQuestionView(@PathVariable Long questionNum, Model model) {
		as.setPageName(model, ServiceMessages.QuizRace.QUESTION + questionNum);
		model.addAttribute("question", questionRepository.findOne(questionNum));
		return "/view/quizRace/paper";
	}
}