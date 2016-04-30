package org.univ7.webapp.quiz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.univ7.webapp.quiz.security.token.JsonTokenWithKeyVo;

@Entity
@Table(name = "PUBLIC_PRIVATE_KEY_MAPPER")
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PublicPrivateKeyMapper {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "PUBLIC_KEY")
	private String publicKey;

	@Column(name = "PRIVATE_KEY")
	private String privateKey;

	@Column(name = "EXPIRED_TIME")
	private String expiredTime;

	public PublicPrivateKeyMapper(JsonTokenWithKeyVo token, String expiredTime) {
		this.publicKey = token.getPublicKey();
		this.privateKey = token.getPrivateKey();
		this.expiredTime = expiredTime;
	}
}
