package com.smaugslair.thitracker.security;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Optional;

public final class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
        // Util methods only
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
            && !(authentication instanceof AnonymousAuthenticationToken)
            && authentication.isAuthenticated();
    }

    public static User getLoggedInUser(SessionService sessionService) {
        if(isUserLoggedIn()) {
            User user = sessionService.getUser();
            if (user != null) {
                return user;
            }
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String email = principal.getAttribute("email");
            if (email != null) {
                Optional<User> optionalUser = sessionService.getUserRepository().findUserByEmail(email);
                if (optionalUser.isPresent()) {
                    log.info("Logged in user:"+ email);
                    sessionService.setUser(optionalUser.get());
                    return optionalUser.get();
                }
                else {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setAdmin(false);
                    String first = principal.getAttribute("given_name");
                    String last = principal.getAttribute("family_name");

                    String displayName = findAvailableDisplayName(first + last.substring(0, 1), 0, sessionService);

                    newUser.setDisplayName(displayName);
                    newUser.setFriendCode(generateFriendCode());

                    log.info("Added and logged in new user:"+ email);
                    newUser = sessionService.getUserRepository().save(newUser);
                    sessionService.setUser(newUser);
                    return newUser;
                }
            }
        }
        return null;
    }

    private static String findAvailableDisplayName(String displayName, Integer count, SessionService sessionService) {
        log.info("Finding available display name: " + displayName+", count"+count);
        String testname = displayName;
        if (count > 0) {
           testname = displayName + count;
        }
        Optional<User> optionalUser = sessionService.getUserRepository().findUserByDisplayName(testname);
        if (optionalUser.isPresent()) {
            return findAvailableDisplayName(displayName, ++count, sessionService);
        }
        else  {
            return testname;
        }
    }

    private static String generateFriendCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();

    }

    public static void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }


}