package com.smaugslair.thitracker;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EventType;
import com.smaugslair.thitracker.data.powers.PowerRepository;
import com.smaugslair.thitracker.data.powers.PowerSetRepository;
import com.smaugslair.thitracker.data.user.Credentials;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.ThiProperties;
import com.smaugslair.thitracker.websockets.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties(ThiProperties.class)
public class ThiTrackerApplication {

    private static final Logger log = LoggerFactory.getLogger(ThiTrackerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ThiTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(
            AtdRepository repository,
            UserRepository userRepository,
            CredentialsRepository credentialsRepository,
            PowerSetRepository powerSetRepository,
            PowerRepository powerRepository,
            ThiProperties thiProperties) {
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
            //log.info(thiProperties.toString());
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
            }/*
            if (powerRepository.findAll().isEmpty()) {

                Power strength = new Power();
                strength.setName("Strength");
                strength.setSsid("str");
                strength.setMakTaken("maxStr");
                strength.setAbilityMods("Str:1");
                strength.addToPowerSets("Animal", 3);
                strength.addToPowerSets("Combo", 4);

                Power speed = new Power();
                speed.setName("Speed");
                speed.setSsid("spe");
                speed.setMakTaken("maxSpe");
                speed.setAbilityMods("Spe:1");
                speed.addToPowerSets("Flash", 2);
                speed.addToPowerSets("Combo", 2);

                PowerSet animal = new PowerSet();
                animal.setName("Animal");
                animal.setSsid("animal");
                animal.setAbilityText("ability");
                animal.setOpenText("open");
                animal.setPowersText("powers");
                animal.setAbilityMods("Str:1");

                PowerSet flash = new PowerSet();
                flash.setName("Flash");
                flash.setSsid("flash");
                flash.setAbilityText("ability");
                flash.setOpenText("open");
                flash.setPowersText("powers");
                flash.setAbilityMods("Spe:1");

                PowerSet combo = new PowerSet();
                combo.setName("Combo");
                combo.setSsid("combo");
                combo.setAbilityText("ability");
                combo.setOpenText("open");
                combo.setPowersText("powers");
                combo.setAbilityMods("Spe:1|Str:1");

                powerRepository.save(speed);
                powerRepository.save(strength);

                powerSetRepository.save(animal);
                powerSetRepository.save(flash);
                powerSetRepository.save(combo);
            }*/
            final Entry ping = new Entry();
            ping.setType(EventType.Ping);
            ScheduledExecutorService keepAlive = Executors.newScheduledThreadPool(1);
            Runnable r = () -> {
                Broadcaster.broadcast(ping);
                //log.info("pinging websockets");
            };
            keepAlive.scheduleAtFixedRate(r, 0, 50, TimeUnit.SECONDS);


        };
    }

}
