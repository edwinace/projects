package org.univ7.webapp.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USER_AUTHORITIES")
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserAuthority {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "USER_ID")
	private String userId;

	@Transient
	@OneToOne(mappedBy = "userAuthority")
	private User user;

	@Column(name = "AUTHORITY")
	private String authority;
}
