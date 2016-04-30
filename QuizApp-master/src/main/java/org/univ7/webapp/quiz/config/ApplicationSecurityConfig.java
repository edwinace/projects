package org.univ7.webapp.quiz.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.univ7.webapp.quiz.filter.JwtRememberMeFilter;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ROLE_PREFIX = "ROLE_";

	private static final String AUTHORITIES_BY_USER_NAME_QUERY = "select USER_ID as principal, AUTHORITY as role from USER_AUTHORITIES where USER_ID = ?";

	private static final String USERS_BY_USER_NAME_QUERY = "select USER_ID as principal, PASSWORD as credentials, true from USER where USER_ID = ?";

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JwtRememberMeFilter jwtRememberMeFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(USERS_BY_USER_NAME_QUERY)
				.authoritiesByUsernameQuery(AUTHORITIES_BY_USER_NAME_QUERY).rolePrefix(ROLE_PREFIX);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/login").permitAll();
		http.authorizeRequests().antMatchers("/service-worker.js").permitAll();
		http.authorizeRequests().antMatchers("/manifest.json").permitAll();
		http.authorizeRequests().antMatchers("/api/private/**").authenticated();
		http.authorizeRequests().antMatchers("/api/**").permitAll();
		http.logout().logoutSuccessUrl("/");
		http.csrf().disable();

		http.addFilterBefore(jwtRememberMeFilter, BasicAuthenticationFilter.class);
	}
}
