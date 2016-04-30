package org.univ7.webapp.quiz.security.token;

public interface TokenExpirationStrategy {
	String getExpiredTime();
	String getTokenDuration();
}
