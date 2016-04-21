/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.config.annotation.web.configurers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageViewFilter;

/**
 * Adds logout support. Other {@link SecurityConfigurer} instances may invoke
 * {@link #addLogoutHandler(LogoutHandler)} in the
 * {@link #init(HttpSecurity)} phase.
 *
 * <h2>Security Filters</h2>
 *
 * The following Filters are populated
 *
 * <ul>
 * <li>
 * {@link LogoutFilter}</li>
 * </ul>
 *
 * <h2>Shared Objects Created</h2>
 *
 * No shared Objects are created
 *
 * <h2>Shared Objects Used</h2>
 *
 * No shared objects are used.
 *
 * @author Rob Winch
 * @since 3.2
 * @see RememberMeConfigurer
 */
public final class LogoutConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<H> {
    private List<LogoutHandler> logoutHandlers = new ArrayList<LogoutHandler>();
    private SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
    private String logoutSuccessUrl = "/login?logout";
    private LogoutSuccessHandler logoutSuccessHandler;
    private String logoutUrl = "/logout";
    private boolean permitAll;
    private boolean customLogoutSuccess;

    /**
     * Creates a new instance
     * @see HttpSecurity#logout()
     */
    public LogoutConfigurer() {
    }

    /**
     * Adds a {@link LogoutHandler}. The {@link SecurityContextLogoutHandler} is
     * added as the last {@link LogoutHandler} by default.
     *
     * @param logoutHandler the {@link LogoutHandler} to add
     * @return the {@link LogoutConfigurer} for further customization
     */
    public LogoutConfigurer<H> addLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandlers.add(logoutHandler);
        return this;
    }

    /**
     * Configures {@link SecurityContextLogoutHandler} to invalidate the {@link HttpSession} at the time of logout.
     * @param invalidateHttpSession true if the {@link HttpSession} should be invalidated (default), or false otherwise.
     * @return the {@link LogoutConfigurer} for further customization
     */
    public LogoutConfigurer<H> invalidateHttpSession(boolean invalidateHttpSession) {
        contextLogoutHandler.setInvalidateHttpSession(invalidateHttpSession);
        return this;
    }

    /**
     * The URL that triggers logout to occur. The default is "/logout"
     * @param logoutUrl the URL that will invoke logout.
     * @return the {@link LogoutConfigurer} for further customization
     */
    public LogoutConfigurer<H> logoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    /**
     * The URL to redirect to after logout has occurred. The default is
     * "/login?logout". This is a shortcut for invoking
     * {@link #logoutSuccessHandler(LogoutSuccessHandler)} with a
     * {@link SimpleUrlLogoutSuccessHandler}.
     *
     * @param logoutSuccessUrl
     *            the URL to redirect to after logout occurred
     * @return the {@link LogoutConfigurer} for further customization
     */
    public LogoutConfigurer<H> logoutSuccessUrl(String logoutSuccessUrl) {
        this.customLogoutSuccess = true;
        this.logoutSuccessUrl = logoutSuccessUrl;
        return this;
    }

    /**
     * A shortcut for {@link #permitAll(boolean)} with <code>true</code> as an argument.
     * @return the {@link LogoutConfigurer} for further customizations
     */
    public LogoutConfigurer<H> permitAll() {
        return permitAll(true);
    }

    /**
     * Allows specifying the names of cookies to be removed on logout success.
     * This is a shortcut to easily invoke
     * {@link #addLogoutHandler(LogoutHandler)} with a
     * {@link CookieClearingLogoutHandler}.
     *
     * @param cookieNamesToClear the names of cookies to be removed on logout success.
     * @return the {@link LogoutConfigurer} for further customization
     */
    public LogoutConfigurer<H> deleteCookies(String... cookieNamesToClear) {
        return addLogoutHandler(new CookieClearingLogoutHandler(cookieNamesToClear));
    }

    /**
     * Sets the {@link LogoutSuccessHandler} to use. If this is specified,
     * {@link #logoutSuccessUrl(String)} is ignored.
     *
     * @param logoutSuccessHandler
     *            the {@link LogoutSuccessHandler} to use after a user has been
     *            logged out.
     * @return the {@link LogoutConfigurer} for further customizations
     */
    public LogoutConfigurer<H> logoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessUrl = null;
        this.customLogoutSuccess = true;
        this.logoutSuccessHandler = logoutSuccessHandler;
        return this;
    }

    /**
     * Grants access to the {@link #logoutSuccessUrl(String)} and the {@link #logoutUrl(String)} for every user.
     *
     * @param permitAll if true grants access, else nothing is done
     * @return the {@link LogoutConfigurer} for further customization.
     */
    public LogoutConfigurer<H> permitAll(boolean permitAll) {
        this.permitAll = permitAll;
        return this;
    }

    /**
     * Gets the {@link LogoutSuccessHandler} if not null, otherwise creates a
     * new {@link SimpleUrlLogoutSuccessHandler} using the
     * {@link #logoutSuccessUrl(String)}.
     *
     * @return the {@link LogoutSuccessHandler} to use
     */
    private LogoutSuccessHandler getLogoutSuccessHandler() {
        if(logoutSuccessHandler != null) {
            return logoutSuccessHandler;
        }
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl(logoutSuccessUrl);
        return logoutSuccessHandler;
    }

    @Override
    public void init(H http) throws Exception {
        if(permitAll) {
            PermitAllSupport.permitAll(http, this.logoutUrl, this.logoutSuccessUrl);
        }

        DefaultLoginPageViewFilter loginPageGeneratingFilter = http.getSharedObject(DefaultLoginPageViewFilter.class);
        if(loginPageGeneratingFilter != null && !isCustomLogoutSuccess()) {
            loginPageGeneratingFilter.setLogoutSuccessUrl(getLogoutSuccessUrl());
        }
    }

    @Override
    public void configure(H http) throws Exception {
        LogoutFilter logoutFilter = createLogoutFilter();
        http.addFilter(logoutFilter);
    }

    /**
     * Returns true if the logout success has been customized via
     * {@link #logoutSuccessUrl(String)} or
     * {@link #logoutSuccessHandler(LogoutSuccessHandler)}.
     *
     * @return true if logout success handling has been customized, else false
     */
    private boolean isCustomLogoutSuccess() {
        return customLogoutSuccess;
    }

    /**
     * Gets the logoutSuccesUrl or null if a
     * {@link #logoutSuccessHandler(LogoutSuccessHandler)} was configured.
     *
     * @return the logoutSuccessUrl
     */
    private String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    /**
     * Creates the {@link LogoutFilter} using the {@link LogoutHandler}
     * instances, the {@link #logoutSuccessHandler(LogoutSuccessHandler)} and
     * the {@link #logoutUrl(String)}.
     *
     * @return the {@link LogoutFilter} to use.
     * @throws Exception
     */
    private LogoutFilter createLogoutFilter() throws Exception {
        logoutHandlers.add(contextLogoutHandler);
        LogoutHandler[] handlers = logoutHandlers.toArray(new LogoutHandler[logoutHandlers.size()]);
        LogoutFilter result = new LogoutFilter(getLogoutSuccessHandler(), handlers);
        result.setFilterProcessesUrl(logoutUrl);
        result = postProcess(result);
        return result;
    }
}