package com.smaugslair.thitracker;

import com.smaugslair.thitracker.data.atd.ActionTimeDefault;
import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableCaching
public class ThiTrackerApplication {

    private static final Logger log = LoggerFactory.getLogger(ThiTrackerApplication.class);



    public static void main(String[] args) {
        log.info("STARTING THITRACKER");
        SpringApplication.run(ThiTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(
            AtdRepository repository,
            UserRepository userRepository
            ) {
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
            else {
                log.info("Database has already been loaded");
            }
            //log.info(thiProperties.toString());
            if (userRepository.findAll().isEmpty()) {
                User user = new User();
                user.setDisplayName("Kevin F");
                user.setEmail("naganalf@gmail.com");
                user.setAdmin(true);
                user.setFriendCode("9876");
                user = userRepository.save(user);


                user = new User();
                user.setDisplayName("Andy A");
                user.setEmail("andya@giantsdancegames.com");
                user.setAdmin(true);
                user.setFriendCode("4321");
                user = userRepository.save(user);

            }/*
            List<Game> games = gameRepository.findAll();
            games.forEach(game -> {
                if (game.getMaxDice() == null) {
                    game.setMaxDice(10);
                    gameRepository.save(game);
                }
            });*/
/*
            final Entry ping = new Entry();
            ping.setType(EventType.Ping);
            ScheduledExecutorService keepAlive = Executors.newScheduledThreadPool(1);
            Runnable r = () -> {
                Broadcaster.broadcast(ping);
                //log.info("pinging websockets");
            };
            keepAlive.scheduleAtFixedRate(r, 0, 50, TimeUnit.SECONDS);*/


        };
    }


/*
Need to turn this on if this is a WAR deployment
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<Cache>();
        caches.add(new ConcurrentMapCache("atds"));
        caches.add(new ConcurrentMapCache("playerCharacters"));
        caches.add(new ConcurrentMapCache("collectedItems"));
        caches.add(new ConcurrentMapCache("friends"));
        caches.add(new ConcurrentMapCache("users"));
        cacheManager.setCaches(caches);
        return cacheManager;
    }*/
}
