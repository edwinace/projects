package org.univ7.webapp.quiz.security.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JsonTokenWithKeyVo {
	private String publicKey;
	private String privateKey;
	private String encryptedToken;
}
