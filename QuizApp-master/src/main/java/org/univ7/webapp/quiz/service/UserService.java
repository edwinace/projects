package org.univ7.webapp.quiz.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.univ7.webapp.quiz.entity.PublicPrivateKeyMapper;
import org.univ7.webapp.quiz.entity.User;
import org.univ7.webapp.quiz.repository.PublicPrivateKeyMapperRepository;
import org.univ7.webapp.quiz.repository.UserRepository;
import org.univ7.webapp.quiz.security.token.JsonTokenVo;
import org.univ7.webapp.quiz.security.token.JsonTokenWithKeyVo;
import org.univ7.webapp.quiz.security.token.TokenExpirationStrategy;
import org.univ7.webapp.quiz.util.DateManager;
import org.univ7.webapp.quiz.util.JsonTokenUtil;

@Service
@Transactional
public class UserService {
//	private final String jsonTemplate = "{\"id\":%d,\"expired\":\"%s\"}";
	private final String jsonTemplate =  "%s";

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PublicPrivateKeyMapperRepository publicPrivateKeyMapperRepository;

	@Autowired
	private DateManager dateManager;

	@Resource(name = "threeDaysExpiration")
	private TokenExpirationStrategy tokenExpirationStrategy;

	public User findUser(User user) {
		return userRepository.findByUserId(user.getUserId());
	}

	public JsonTokenVo getToken(User user) {
		JsonTokenWithKeyVo token = createToken(user.getId());

		PublicPrivateKeyMapper mapper = new PublicPrivateKeyMapper(token, getExpiredTime());
		publicPrivateKeyMapperRepository.save(mapper);
		return new JsonTokenVo(mapper.getPublicKey(), token.getEncryptedToken(), getExpiredTime(), getTokenDuration());
	}

	private JsonTokenWithKeyVo createToken(Long id) {
		String tokenMessage = String.format(jsonTemplate, id);
		return JsonTokenUtil.createJweToken(tokenMessage);
	}

	private String getExpiredTime() {
		return tokenExpirationStrategy.getExpiredTime();
	}
	
	private String getTokenDuration() {
		return tokenExpirationStrategy.getTokenDuration();
	}
}
