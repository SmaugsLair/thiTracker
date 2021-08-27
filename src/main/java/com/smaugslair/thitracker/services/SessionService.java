package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.powers.PowerRepository;
import com.smaugslair.thitracker.data.powers.PowerSetRepository;
import com.smaugslair.thitracker.data.user.*;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class SessionService {

    //private final CacheService cacheService;
    private Long gameId;
    private PlayerCharacter pc;

    private JavaMailSender javaMailSender;
    private ThiProperties thiProperties;
    private PowersCache powersCache;

    private AtdRepository atdRepo;
    private CollectedItemRepository ciRepo;
    private CredentialsRepository credRepo;
    private PowerRepository powerRepo;
    private PowerSetRepository powerSetRepo;
    private PasswordResetRepository passwordResetRepo;
    private FriendshipRepository friendsRepo;
    private UserRepository userRepository;
    private TimeLineItemRepository tliRepo;
    private GameRepository gameRepo;
    private PlayerCharacterRepository pcRepo;
    private EntryRepository entryRepo;


    public SessionService(/*CacheService cacheService*/) {
        /*this.cacheService = cacheService;*/
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
            pc = pcRepo.findById(pc.getId()).orElse(pc);
            //pc= cacheService.getPcCache().findOneById(pc.getId()).orElse(pc);
        }
    }



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
/*
    public CacheService getCacheService() {
        return cacheService;
    }*/


   public AtdRepository getAtdRepo() {
       return atdRepo;
   }

   @Autowired
   public void setAtdRepo(AtdRepository atdRepository) {
       this.atdRepo = atdRepository;
   }

    public CollectedItemRepository getCiRepo() {
        return ciRepo;
    }

    @Autowired
    public void setCiRepo(CollectedItemRepository ciRepo) {
        this.ciRepo = ciRepo;
    }

    public TimeLineItemRepository getTliRepo() {
        return tliRepo;
    }

    @Autowired
    public void setTliRepo(TimeLineItemRepository tliRepo) {
        this.tliRepo = tliRepo;
    }

    public GameRepository getGameRepo() {
        return gameRepo;
    }

    @Autowired
    public void setGameRepo(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    public PlayerCharacterRepository getPcRepo() {
        return pcRepo;
    }

    @Autowired
    public void setPcRepo(PlayerCharacterRepository pcRepo) {
        this.pcRepo = pcRepo;
    }

    public EntryRepository getEntryRepo() {
        return entryRepo;
    }

    @Autowired
    public void setEntryRepo(EntryRepository entryRepo) {
        this.entryRepo = entryRepo;
    }
}