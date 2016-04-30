package org.univ7.webapp.quiz.security.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JsonTokenVo {
	private String pubKey;
	private String token;
	private String expiredDate;
	private String tokenDuration;
}
