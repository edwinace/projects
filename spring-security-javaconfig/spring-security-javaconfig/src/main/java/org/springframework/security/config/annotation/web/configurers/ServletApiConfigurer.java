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

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

/**
 * Implements select methods from the {@link HttpServletRequest} using the {@link SecurityContext} from the {@link SecurityContextHolder}.
 *
 * <h2>Security Filters</h2>
 *
 * The following Filters are populated
 *
 * <ul>
 * <li>{@link SecurityContextHolderAwareRequestFilter}</li>
 * </ul>
 *
 * <h2>Shared Objects Created</h2>
 *
 * No shared objects are created.
 *
 * <h2>Shared Objects Used</h2>
 *
 * No shared Objects are used.
 *
 * @author Rob Winch
 * @since 3.2
 */
public final class ServletApiConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<H> {
    private SecurityContextHolderAwareRequestFilter securityContextRequestFilter = new SecurityContextHolderAwareRequestFilter();

    /**
     * Creates a new instance
     * @see HttpSecurity#servletApi()
     */
    public ServletApiConfigurer() {
    }

    public ServletApiConfigurer<H> rolePrefix(String rolePrefix) {
        securityContextRequestFilter.setRolePrefix(rolePrefix);
        return this;
    }

    @SuppressWarnings("unchecked")
    public H disable() {
        getBuilder().removeConfigurer(getClass());
        return getBuilder();
    }

    @Override
    public void configure(H builder)
            throws Exception {
        securityContextRequestFilter = postProcess(securityContextRequestFilter);
        builder.addFilter(securityContextRequestFilter);
    }
}