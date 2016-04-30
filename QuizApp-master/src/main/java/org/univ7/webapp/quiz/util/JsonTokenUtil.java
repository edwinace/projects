package org.univ7.webapp.quiz.util;

import java.security.Key;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.univ7.webapp.exception.JweTokenCreateException;
import org.univ7.webapp.exception.JweTokenDecryptException;
import org.univ7.webapp.quiz.security.token.JsonTokenWithKeyVo;

public class JsonTokenUtil {

	public static JsonTokenWithKeyVo createJweToken(String encryptTargetString) throws JweTokenCreateException {
		if (encryptTargetString == null || "".equals(encryptTargetString)) {
			throw new JweTokenCreateException("암호화할 토큰의 메시지는 공백 또는 null일수 없습니다");
		}

		String stringPrivateKey = UuidGenerator.createDouubleUUID();
		Key privateKey = new AesKey(stringPrivateKey.getBytes());
		
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload(encryptTargetString);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS256_A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(privateKey);

		String serializedJwe = null;
		try {
			serializedJwe = jwe.getCompactSerialization();
		} catch (JoseException e) {
			throw new JweTokenCreateException("암호화된 Token문자열(Jwe)를 직렬화 하는 과정에서 문제가 발생하였습니다");
		}
		
		String publicKey = UuidGenerator.createDouubleUUID();
		return new JsonTokenWithKeyVo(publicKey, stringPrivateKey, serializedJwe);
	}

	public static String decryptToken(String stringKey, String decryptTargetStringToken) throws JweTokenDecryptException {
		if (stringKey == null || decryptTargetStringToken == null || "".equals(decryptTargetStringToken)) {
			throw new JweTokenCreateException("암호화할 토큰의 메시지는 공백 또는 null일수 없습니다");
		}
		Key key = new AesKey(stringKey.getBytes());
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setKey(key);
		String decryptedToken = null;

		try {
			jwe.setCompactSerialization(decryptTargetStringToken);
			decryptedToken = jwe.getPayload();
		} catch (JoseException e) {
			throw new JweTokenDecryptException("암호화된 Token문자열(Jwe)을 복호화 하는 과정에서 문제가 발생하였습니다");
		}
		return decryptedToken;
	}
}
