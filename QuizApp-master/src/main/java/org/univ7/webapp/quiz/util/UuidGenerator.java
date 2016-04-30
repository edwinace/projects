package org.univ7.webapp.quiz.util;

import java.util.UUID;

public class UuidGenerator {
	public static String createUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String createDouubleUUID() {
		return createUUID() + createUUID();
	}
}
