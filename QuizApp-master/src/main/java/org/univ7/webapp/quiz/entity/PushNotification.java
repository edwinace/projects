package org.univ7.webapp.quiz.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.univ7.webapp.quiz.util.HttpClient;

@Entity
@Table(name = "PUSH_NOTIFICATION")
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class PushNotification {
	private static final String GCM_URI = "https://gcm-http.googleapis.com/gcm/send";
	private static final String MESSAGE_FORMAT = "{\"registration_ids\":[\"%s\"]}";

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@OneToOne(mappedBy = "pushNotification")
	private User user;

	@Column(name = "GCM_REGISTRATION_ID")
	private String gcmRegistrationId;

	public PushNotification(User user, String gcmRegistrationId) {
		this.user = user;
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public Boolean sendPush(HttpClient httpClient) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "key=AIzaSyAbaM3Sp9DbNO3hDk2PbU7sAdrmwfjTg78");
		headers.put("Content-Type", "application/json");

		String result = httpClient.sendJsonPost(GCM_URI, headers, String.format(MESSAGE_FORMAT, this.gcmRegistrationId));
		return result.contains("\"success\":1,");
	}
}