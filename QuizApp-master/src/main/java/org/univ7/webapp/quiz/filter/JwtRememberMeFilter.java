package org.univ7.webapp.quiz.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.univ7.webapp.quiz.entity.PublicPrivateKeyMapper;
import org.univ7.webapp.quiz.enums.DateFormat;
import org.univ7.webapp.quiz.repository.PublicPrivateKeyMapperRepository;
import org.univ7.webapp.quiz.repository.UserRepository;
import org.univ7.webapp.quiz.security.AuthorityManager;
import org.univ7.webapp.quiz.util.DateManager;
import org.univ7.webapp.quiz.util.JsonTokenUtil;

@Component
public class JwtRememberMeFilter implements Filter {
	@Autowired
	private PublicPrivateKeyMapperRepository publicPrivateKeyMapperRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateManager dateManager;
	@Autowired
	private AuthorityManager authorityManager;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = ((HttpServletRequest) servletRequest);
		HttpServletResponse response = ((HttpServletResponse) servletResponse);

		String requestUrl = request.getRequestURI();

		if (requestUrl.contains("/api/private/")) {
			HttpSession session = request.getSession();
			String token = extractData(request, "token");
			String publicKey = extractData(request, "pubKey");

			if (session.getAttribute("user") == null) {
				PublicPrivateKeyMapper mapper = publicPrivateKeyMapperRepository.findByPublicKey(publicKey);
				if (mapper != null && dateManager.isFuture(mapper.getExpiredTime(), DateFormat.LocalDateTime) && session.getAttribute("user") == null) {
					Long userNumId = Long.parseLong(JsonTokenUtil.decryptToken(mapper.getPrivateKey(), token));
					session.setAttribute("user", userRepository.findOne(userNumId));
					authorityManager.setLoginStatusOnSessionAndContext(userNumId, session);
				}
			}
		}
		chain.doFilter(request, response);
	}

	private String extractData(HttpServletRequest request, String CookieKey) {
		// 쿠키 배열에 쿠키값 가져오기
		Cookie[] cookies = request.getCookies();
		String cookieValue = null;

		for (Cookie cookie : cookies) {
			if (CookieKey.equals(cookie.getName())) {
				cookieValue = cookie.getValue().replace("%22", "");
			}
		}
		return cookieValue;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
