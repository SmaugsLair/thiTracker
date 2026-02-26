package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@VaadinSessionScope
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    @Value("${thi.appurl}")
    private String appUrl;

    private User user;


    private final UserRepository userRepository;

    public SessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public User getLoggedInUser() {
        if(isUserLoggedIn()) {
            if (user != null) {
                return user;
            }
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String email = principal.getAttribute("email");
            if (email != null) {
                Optional<User> optionalUser = userRepository.findUserByEmail(email);
                if (optionalUser.isPresent()) {
                    log.info("Logged in user:{}", email);
                    user = optionalUser.get();
                }
                else {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setAdmin(false);
                    String first = principal.getAttribute("given_name");
                    String last = principal.getAttribute("family_name");

                    String displayName = findAvailableDisplayName(first + last.charAt(0), 0, userRepository);

                    newUser.setDisplayName(displayName);
                    newUser.setFriendCode(generateFriendCode());

                    log.info("Added and logged in new user:{}", email);
                    newUser = userRepository.save(newUser);
                    user = newUser;
                }
                return user;
            }
        }
        return null;
    }

    private String findAvailableDisplayName(String displayName, Integer count, UserRepository userRepository) {
        log.info("Finding available display name: {}, count:{}", displayName, count);
        String testname = displayName;
        if (count > 0) {
            testname = displayName + count;
        }
        Optional<User> optionalUser = userRepository.findUserByDisplayName(testname);
        if (optionalUser.isPresent()) {
            return findAvailableDisplayName(displayName, ++count, userRepository);
        }
        else  {
            return testname;
        }
    }

    private String generateFriendCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();

    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}