package com.smaugslair.thitracker;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ThiTrackerApplication {

    private static final Logger log = LoggerFactory.getLogger(ThiTrackerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ThiTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(
            AtdRepository repository,
            UserRepository userRepository,
            CredentialsRepository credentialsRepository) {
        return (args) -> {

            if (repository.findAll().isEmpty()) {
                repository.save(new ActionTimeDefault("Thought", 3, true));
                repository.save(new ActionTimeDefault("Twitch", 4, true));
                repository.save(new ActionTimeDefault("Move", 5, true));
                repository.save(new ActionTimeDefault("Perception", 5, true));
                repository.save(new ActionTimeDefault("Targeted", 7, true));
                repository.save(new ActionTimeDefault("Full Round", 10, true));
                repository.save(new ActionTimeDefault("Recovery", 10, false));
                repository.save(new ActionTimeDefault("Repair", 10, true));
            }
            if (userRepository.findAll().isEmpty()) {
                User user = new User();
                user.setName("naganalf");
                user.setDisplayName("Kevin F");
                user.setEmail("naganalf@gmail.com");
                user.setAdmin(true);
                user.setFriendCode("9876");
                user = userRepository.save(user);

                Credentials credentials = new Credentials();
                credentials.setUserId(user.getId());
                credentials.setEncodedPassword(SecurityUtils.encode("password"));
                credentialsRepository.save(credentials);

                user = new User();
                user.setName("deadandy");
                user.setDisplayName("Andy A");
                user.setEmail("andya@giantsdancegames.com");
                user.setAdmin(true);
                user.setFriendCode("4321");
                user = userRepository.save(user);

                credentials = new Credentials();
                credentials.setUserId(user.getId());
                credentials.setEncodedPassword(SecurityUtils.encode("password"));
                credentialsRepository.save(credentials);
            }
        };
    }

}
