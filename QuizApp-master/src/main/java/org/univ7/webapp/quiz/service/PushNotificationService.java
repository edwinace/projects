package org.univ7.webapp.quiz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.univ7.webapp.quiz.entity.PushNotification;
import org.univ7.webapp.quiz.entity.User;
import org.univ7.webapp.quiz.repository.PushNotificationRepository;
import org.univ7.webapp.quiz.util.HttpClient;

@Service
@Transactional
public class PushNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
	
	@Autowired
	private PushNotificationRepository pushNotificationRepository;
	@Autowired
	private HttpClient httpClient;
	
	public void addSubscription(User user, String subscriptionId) {
		PushNotification notification = pushNotificationRepository.findByGcmRegistrationId(subscriptionId);
		if (notification == null) {
			notification = new PushNotification(user, subscriptionId);
			pushNotificationRepository.save(notification);
			user.setPushNotification(notification);
		}
	}

	public void deleteSubscription(User user, String subscriptionId) {
		PushNotification notification = pushNotificationRepository.findByGcmRegistrationId(subscriptionId);
		if (notification != null) {
			user.setPushNotification(null);
			pushNotificationRepository.delete(notification);
		}
	}

	public void sendPushMessage() {
		List<PushNotification> subScriptionList = pushNotificationRepository.findAll();
		for (PushNotification noti : subScriptionList) {
			boolean success = noti.sendPush(httpClient);
			logger.debug("메시지 발송 성공 여부 : {}", success ? "발송성공" : "발송실패");
		}
	}
}
