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

    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //String username = ;
        String password = authentication.getCredentials().toString();

        User user = userRepository.findUserByName(authentication.getName());
        Credentials credentials = credentialsRepository.findByUserId(user.getId());
        if (user != null) {
            if (SecurityUtils.matches(authentication.getCredentials().toString(), credentials.getEncodedPassword())) {
                return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
            }
        }
        throw new BadCredentialsException("NOPE");
    }

    @Override
    public boolean supports(Class<?>aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}