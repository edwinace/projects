package org.univ7.webapp.quiz.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.univ7.webapp.quiz.entity.User;

public class UserTest {

	@Test
	public void loginTest() {
		User loginRequestUser = new User(null, "userId11", "password");
		User userFromDataBase = new User(null, "userId22", "password");
		assertFalse("Id가 달라서 로그인에 실패해야 한다", loginRequestUser.login(userFromDataBase));
		
		userFromDataBase = new User(null, "userId11", "password22");
		assertFalse("pw가 달라서 로그인에 실패해야 한다", loginRequestUser.login(userFromDataBase));
		
		userFromDataBase = new User(null, "userId11", "password");
		assertTrue("Id와 pw가 같으므로 로그인에 성공해야 한다", loginRequestUser.login(userFromDataBase));
	}
}
