package org.univ7.webapp.quiz.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.univ7.webapp.quiz.repository.UserAuthorityRepository;

@Component
public class AuthorityManager {
	private static final Logger logger = LoggerFactory.getLogger(AuthorityManager.class);
	
	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	public void setLoginStatusOnSessionAndContext(Long uId, HttpSession session) {
		setAuthorityInfoOnContext(uId);
		setAuthorityInfoOnSession(uId, session);
	}

	private void setAuthorityInfoOnContext(Long uId) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(userAuthorityRepository.findOne(uId).getAuthority()));
		Authentication auth = new UsernamePasswordAuthenticationToken(uId, null, authorities);
		setAuthentication(auth);
		
		logger.info("바뀐 권한 : {}", SecurityContextHolder.getContext().getAuthentication());
	}

	private void setAuthorityInfoOnSession(Long uId, HttpSession session) {
		session.setAttribute("userAuthority", userAuthorityRepository.findOne(uId).getAuthority());
	}

	private void setAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
