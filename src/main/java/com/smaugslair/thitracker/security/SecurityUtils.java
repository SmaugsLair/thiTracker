package com.smaugslair.thitracker.security;

import com.smaugslair.thitracker.data.user.User;
import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

public final class SecurityUtils {

    private static Logger log = LoggerFactory.getLogger(SecurityUtils.class);
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private SecurityUtils() {
        // Util methods only
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {

        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
            && Stream.of(RequestType.values())
            .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
            && !(authentication instanceof AnonymousAuthenticationToken)
            && authentication.isAuthenticated();
    }

    public static User getLoggedInUser() {
        if(isUserLoggedIn()) {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    /*private final static String passwordReset = "/passwordreset";
    public static boolean isPasswordReset(HttpServletRequest request) {
        log.info("isPasswordReset getServletPath: "+ request.getServletPath());
        return passwordReset.equals(request.getServletPath());
    }*/
}