package org.univ7.webapp.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.univ7.webapp.quiz.entity.PushNotification;

public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
	PushNotification findByGcmRegistrationId(String subscriptionId);
}
