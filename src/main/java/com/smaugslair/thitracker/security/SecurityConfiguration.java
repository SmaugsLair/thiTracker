package com.smaugslair.thitracker.security;

import com.smaugslair.thitracker.ui.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity 
@Configuration 
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable().requestCache().requestCache(new CustomRequestCache());
        /*http.authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated();*/
       /* http.formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL);
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL).deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);*/
        /*http.authorizeHttpRequests().
                requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated();*/
        super.configure(http);
        setLoginView(http, LoginView.class);

    }
   /* public void configure(WebSecurity web) {
        web.
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/resetpassword",
                "/h2-console/**",
                "/offline-stub.html",
                "/sw-runtime-resources-precache.js");
    }*/
}