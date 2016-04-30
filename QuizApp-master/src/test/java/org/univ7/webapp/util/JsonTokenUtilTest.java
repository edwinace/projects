package org.univ7.webapp.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.univ7.webapp.quiz.security.token.JsonTokenWithKeyVo;
import org.univ7.webapp.quiz.util.JsonTokenUtil;

public class JsonTokenUtilTest {
	
	@Test
	public void 키_생성_테스트() {
		String encryptTarget = "Hello World!";
		
		JsonTokenWithKeyVo jsonToken = JsonTokenUtil.createJweToken(encryptTarget);
		String encryptedToken = jsonToken.getEncryptedToken();
		assertNotNull("암호화된 토큰은 Null이면 안된다", encryptedToken);
		assertNotEquals("암호화된 토큰은 빈 문자열이면 안된다", "", encryptedToken);
	}
	
	@Test
	public void 키_복호화_테스트() throws JoseException {
		String encryptTarget = "Hello World!";
		JsonTokenWithKeyVo jsonToken = JsonTokenUtil.createJweToken(encryptTarget);
		String encryptedToken = jsonToken.getEncryptedToken();

		String decryptedToken = JsonTokenUtil.decryptToken(jsonToken.getPrivateKey(), encryptedToken);
		assertThat(decryptedToken, equalTo(encryptTarget));
	}
}
