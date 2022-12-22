/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.web.spring;

import org.devgateway.toolkit.persistence.spring.CustomJPAUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import static org.devgateway.toolkit.web.WebConstants.FORMS_BASE_PATH;

/**
 * @author mpostelnicu This configures the spring security for the Web project.
 * An
 */

@Configuration
@ConditionalOnMissingClass("org.devgateway.toolkit.forms.FormsSecurityConfig")
@Order(2) // this loads the security config after the forms security (if you use
// them overlayed, it must pick that one first)
@PropertySource("classpath:allowedApiEndpoints.properties")
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    protected CustomJPAUserDetailsService customJPAUserDetailsService;

    @Value("${allowedApiEndpoints}")
    private String[] allowedApiEndpoints;

    @Value("${roleHierarchy}")
    private String roleHierarchyStringRepresentation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        final StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextHolderFilter securityContextHolderFilter(
            HttpSessionSecurityContextRepository securityContextRepository) {
        SecurityContextHolderFilter securityContextHolderFilter =
                new SecurityContextHolderFilter(securityContextRepository);
        return securityContextHolderFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return this::configure;
    }

    public void configure(final WebSecurity web) {
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
                .ignoring().antMatchers(allowedApiEndpoints).and()
                .ignoring().antMatchers(
                        FORMS_BASE_PATH + "/login",
                        FORMS_BASE_PATH + "/forgotPassword/**");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
            final SecurityContextHolderFilter securityContextHolderFilter) throws Exception {
        this.configure(http, securityContextHolderFilter);
        return http.build();
    }

    protected void configure(final HttpSecurity http, final SecurityContextHolderFilter securityContextHolderFilter)
            throws Exception {
        http.authorizeRequests().expressionHandler(webExpressionHandler()) // inject role hierarchy
                .antMatchers(FORMS_BASE_PATH + "/monitoring/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(FORMS_BASE_PATH + "/**").authenticated().and()
                .formLogin().loginPage(FORMS_BASE_PATH + "/login").permitAll().and()
                .requestCache().and().logout().permitAll().and()
                .sessionManagement().and().csrf().disable();
        http.addFilter(securityContextHolderFilter);
    }

    /**
     * Instantiates {@see DefaultWebSecurityExpressionHandler} and assigns to it
     * role hierarchy.
     *
     * @return
     */
    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        final DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }

    /**
     * Enable hierarchical roles. This bean can be used to extract all effective
     * roles.
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyStringRepresentation);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customJPAUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
