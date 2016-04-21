package org.springframework.security.oauth.examples.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2ClientContextFilter oauth2ClientFilter;

    @Override
    protected void registerAuthentication(
            AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("marissa").password("wombat").roles("USER").and()
                .withUser("sam").password("kangaroo").roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeUrls()
                .antMatchers("/sparklr/**","/facebook/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
            .addFilterAfter(oauth2ClientFilter, ExceptionTranslationFilter.class)
            .logout()
                .logoutSuccessUrl("/login.jsp")
                .logoutUrl("/logout.do")
                .permitAll()
                .and()
            .formLogin()
                .loginPage("/login.jsp")
                .loginProcessingUrl("/login.do")
                .failureUrl("/login.jsp?authentication_error=true")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();
    }
}
