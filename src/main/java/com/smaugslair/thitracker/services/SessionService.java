package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.powers.PowerRepository;
import com.smaugslair.thitracker.data.powers.PowerSetRepository;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.FriendshipRepository;
import com.smaugslair.thitracker.data.user.PasswordResetRepository;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class SessionService {

    private final CacheService cacheService;
    private Long gameId;
    private PlayerCharacter pc;

    public SessionService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public PlayerCharacter getPc() {
        return pc;
    }
    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
    }

    public void refreshPc() {
        if (pc != null) {
            pc= cacheService.getPcCache().findOneById(pc.getId()).orElse(pc);
        }
    }

    private CredentialsRepository credRepo;
    private PowerRepository powerRepo;
    private PowerSetRepository powerSetRepo;
    private PasswordResetRepository passwordResetRepo;
    private JavaMailSender javaMailSender;
    private ThiProperties thiProperties;
    private PowersCache powersCache;
    private FriendshipRepository friendsRepo;
    private UserRepository userRepository;


    public CredentialsRepository getCredRepo() {
        return credRepo;
    }
    @Autowired
    public void setCredRepo(CredentialsRepository credRepo) {
        this.credRepo = credRepo;
    }

    public FriendshipRepository getFriendsRepo() {
        return friendsRepo;
    }
    @Autowired
    public void setFriendsRepo(FriendshipRepository friendsRepo) {
        this.friendsRepo = friendsRepo;
    }

    public PowerRepository getPowerRepo() {
        return powerRepo;
    }
    @Autowired
    public void setPowerRepo(PowerRepository powerRepo) {
        this.powerRepo = powerRepo;
    }

    public PowerSetRepository getPowerSetRepo() {
        return powerSetRepo;
    }
    @Autowired
    public void setPowerSetRepo(PowerSetRepository powerSetRepo) {
        this.powerSetRepo = powerSetRepo;
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }
    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public PasswordResetRepository getPasswordResetRepo() {
        return passwordResetRepo;
    }
    @Autowired
    public void setPasswordResetRepo(PasswordResetRepository passwordResetRepo) {
        this.passwordResetRepo = passwordResetRepo;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ThiProperties getThiProperties() {
        return thiProperties;
    }
    @Autowired
    public void setThiProperties(ThiProperties thiProperties) {
        this.thiProperties = thiProperties;
    }


    public PowersCache getPowersCache() {
        return  powersCache;
    }
    @Autowired
    public void setPowersCache(PowersCache powersCache) {
        this.powersCache = powersCache;
    }

    public CacheService getCacheService() {
        return cacheService;
    }
}