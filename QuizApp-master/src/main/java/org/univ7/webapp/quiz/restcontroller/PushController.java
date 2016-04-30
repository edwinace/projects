package org.univ7.webapp.quiz.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.univ7.webapp.quiz.constant.ServiceMessages;
import org.univ7.webapp.quiz.constant.UriResources;
import org.univ7.webapp.quiz.entity.User;
import org.univ7.webapp.quiz.service.PushNotificationService;
import org.univ7.webapp.quiz.util.AttributeSetter;
import org.univ7.webapp.quiz.util.JSONResponseUtil;
import org.univ7.webapp.quiz.vo.JsonMessageVo;

@Controller
public class PushController {
	private static final Logger logger = LoggerFactory.getLogger(PushController.class);
	@Autowired
	private AttributeSetter as;
	@Autowired
	private PushNotificationService pushNotificationService;
	
	@RequestMapping(value = UriResources.PrivateApi.PUSH_REGISTER, method = RequestMethod.GET)
	public String pushView(Model model) {
		as.setPageName(model, ServiceMessages.Push.PUSH_PAGE_NAME);
		return "/view/push/register";
	}

	@RequestMapping(value = UriResources.PrivateApi.PUSH_REGISTER, method = RequestMethod.POST)
	public ResponseEntity<Object> registerSubscriptionKey(String subscriptionId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		pushNotificationService.addSubscription(user, subscriptionId);
		return successResponse();
	}

	@RequestMapping(value = UriResources.PrivateApi.PUSH_REGISTER + "/{subscriptionId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> unSubscriptionKey(@PathVariable String subscriptionId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		pushNotificationService.deleteSubscription(user, subscriptionId);
		return successResponse();
	}
	
	@RequestMapping(value = UriResources.Api.PUSH_MESSAGE_DATA, method = RequestMethod.GET)
	public ResponseEntity<Object> pushMessageData() {
		return JSONResponseUtil.getJSONResponse(new PushNotiData(), HttpStatus.OK);
	}
	
	static class PushNotiData {
		private String title = "오늘의 퀴즈가 도착했어요~";
		private String message = "오늘의 퀴즈를 풀어볼까요?";
		private String notiTag = "Daily Quiz";
		private String openUrl = "/#/todayQuiz";
		
		public String getTitle() {
			return title;
		}
		public String getMessage() {
			return message;
		}
		public String getNotiTag() {
			return notiTag;
		}
		public String getOpenUrl() {
			return openUrl;
		}
	}
	
	@RequestMapping(value = UriResources.PrivateApi.PUSH_SEND_TEST, method = RequestMethod.GET)
	public ResponseEntity<Object> pushSendTest() {
		pushNotificationService.sendPushMessage();
		return successResponse();
	}
	
	private ResponseEntity<Object> successResponse() {
		JsonMessageVo vo = new JsonMessageVo();
		return JSONResponseUtil.getJSONResponse(vo, HttpStatus.OK);
	}
}