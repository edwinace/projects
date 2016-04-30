package org.univ7.webapp.quiz.studytest;

import java.security.Key;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.junit.Test;

public class JwtStudy {

	@Test
	// https://bitbucket.org/b_c/jose4j/wiki/Home
	public void 공식사이트의_기본_예제() throws JoseException {
		Key key = new AesKey(ByteUtil.randomBytes(16));
		
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload("Hello World!");
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		
		String serializedJwe = jwe.getCompactSerialization();
		jwe = new JsonWebEncryption();
		jwe.setKey(key);
		jwe.setCompactSerialization(serializedJwe);
		
		System.out.println("기본 예제");
		System.out.println("Serialized Encrypted JWE: " + serializedJwe);
		System.out.println("Payload: " + jwe.getPayload());
	}

	@Test
	public void 키의_길이를_늘리고_조금_더_강한_알고리즘을_사용하도록_수정() throws JoseException {
		Key key = new AesKey(ByteUtil.randomBytes(2048));
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload("Hello World!");
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS256_A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key);
		
		String serializedJwe = jwe.getCompactSerialization();
		jwe = new JsonWebEncryption();
		jwe.setKey(key);
		jwe.setCompactSerialization(serializedJwe);
		
		System.out.println("키의_길이를_늘리고_조금_더_강한_알고리즘을_사용하도록_수정");
		System.out.println("Serialized Encrypted JWE: " + serializedJwe);
		System.out.println("Payload: " + jwe.getPayload());
	}

	@Test
	public void 키의_길이를_늘리고_조금_더_강한_알고리즘을_사용하며_DB에_저장하기_쉽게_byte를_String으로_바꾼_예제_작성() throws JoseException {
		// 키를 생성
		Key key = new AesKey(ByteUtil.randomBytes(2048));
		
		// 키가 byte[] 배열로 구성되어 있어서 다루기가 힘들다, 그래서 String으로 변환
		String stringKey = new String(key.getEncoded(), 0, key.getEncoded().length);
		
		// String으로 변환시킨 key를 다시 byte[] 배열로 변환 
		byte[] byteKey = stringKey.getBytes();
		
		// byte[] -> String -> byte[] 한 값으로 Key 생성 
		Key key2 = new AesKey(byteKey);
		
		// 결과는 동일하게 나온다는것을 확인할 수 있다
		// byte[] 를 String으로 바꾸어서 편하게 핸들링 할 수 있게 되었다
		
		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setPayload("Hello World!");
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.PBES2_HS256_A128KW);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(key2);

		String serializedJwe = jwe.getCompactSerialization();
		jwe = new JsonWebEncryption();
		jwe.setKey(key2);
		jwe.setCompactSerialization(serializedJwe);
		
		System.out.println("키의_길이를_늘리고_조금_더_강한_알고리즘을_사용하며_DB에_저장하기_쉽게_byte를_String으로_바꾼_예제_작성");
		System.out.println("Serialized Encrypted JWE: " + serializedJwe);
		System.out.println("Payload: " + jwe.getPayload());
	}
}
