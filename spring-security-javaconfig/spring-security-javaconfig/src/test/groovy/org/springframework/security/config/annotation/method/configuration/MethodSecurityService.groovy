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
package org.springframework.security.config.annotation.method.configuration;

import javax.annotation.security.DenyAll

import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication


/**
 *
 * @author Rob Winch
 */
public interface MethodSecurityService {
    @PreAuthorize("denyAll")
    public String preAuthorize();

    @Secured("ROLE_ADMIN")
    public String secured();

    @DenyAll
    public String jsr250();

    @Secured(["ROLE_USER","RUN_AS_SUPER"])
    public Authentication runAs();

    @PreAuthorize("permitAll")
    public String preAuthorizePermitAll();

    @PreAuthorize("hasPermission(#object,'read')")
    public String hasPermission(String object);

    @PostAuthorize("hasPermission(#object,'read')")
    public String postHasPermission(String object);
}
