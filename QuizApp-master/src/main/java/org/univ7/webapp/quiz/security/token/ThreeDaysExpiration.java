package org.univ7.webapp.quiz.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.univ7.webapp.quiz.util.DateManager;

@Component("threeDaysExpiration")
public class ThreeDaysExpiration implements TokenExpirationStrategy{
	private static final int TOKEN_DURATION = 3;
	
	@Autowired
	private DateManager dateManager;
	
	@Override
	public String getExpiredTime() {
		return dateManager.getFutureDateTime(TOKEN_DURATION);
	}

	@Override
	public String getTokenDuration() {
		return TOKEN_DURATION+"";
	}
}
