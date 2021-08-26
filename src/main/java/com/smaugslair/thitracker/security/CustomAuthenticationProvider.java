package com.smaugslair.thitracker.security;

import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    UserRepository userRepository;
    CredentialsRepository credentialsRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //String username = ;
        String password = authentication.getCredentials().toString();

        User user = userRepository.findUserByName(authentication.getName());
        if (user != null) {
            Credentials credentials = credentialsRepository.findByUserId(user.getId());
            if (SecurityUtils.matches(authentication.getCredentials().toString(), credentials.getEncodedPassword())) {
                log.info(user.getName() + " logged in");
                return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
            }
        }
        throw new BadCredentialsException("NOPE");
    }

    @Override
    public boolean supports(Class<?>aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCredentialsRepository(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }
}