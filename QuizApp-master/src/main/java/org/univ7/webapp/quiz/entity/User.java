package org.univ7.webapp.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USER")
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "UserId must not be null")
	@Column(name = "USER_ID")
	private String userId;

	@NotNull(message = "Password must not be null")
	@Column(name = "PASSWORD")
	private String password;

	@OneToOne
	@JoinColumn(name = "ID")
	@Setter(AccessLevel.NONE)
	private PushNotification pushNotification;

	public User(Long id, String userId, String password) {
		this.id = id;
		this.userId = userId;
		this.password = password;
	}
	
	public boolean login(User user) {
		return (this.userId.equals(user.userId)) && (this.password.equals(user.password));
	}

	public void setPushNotification(PushNotification pushNotification) {
		if (this.pushNotification != null) {
			this.pushNotification.setUser(null);
		}
		if (pushNotification != null) {
			pushNotification.setUser(this);
		}
		this.pushNotification = pushNotification;
	}
}
