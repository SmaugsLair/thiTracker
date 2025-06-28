package com.smaugslair.thitracker.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity 
@Configuration 
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private static final String OAUTH_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.oauth2Login(c -> c.loginPage(OAUTH_URL).permitAll());
    }
}