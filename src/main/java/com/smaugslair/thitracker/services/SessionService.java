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

    private Long gameId;
    private PlayerCharacter pc;

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
        }
    }

    private AtdRepository atdRepo;
    private CredentialsRepository credRepo;
    private UserRepository userRepo;
    private GameRepository gameRepo;
    private TimeLineItemRepository tliRepo;
    private EntryRepository entryRepo;
    private PlayerCharacterRepository pcRepo;
    private CollectedItemRepository ciRepo;
    private FriendshipRepository friendsRepo;
    private PowerRepository powerRepo;
    private PowerSetRepository powerSetRepo;
    private JavaMailSender javaMailSender;
    private PasswordResetRepository passwordResetRepo;
    private ThiProperties thiProperties;
    private PowersCache powersCache;

    public AtdRepository getAtdRepo() {
        return atdRepo;
    }
    @Autowired
    public void setAtdRepo(AtdRepository atdRepository) {
        this.atdRepo = atdRepository;
    }

    public CredentialsRepository getCredRepo() {
        return credRepo;
    }
    @Autowired
    public void setCredRepo(CredentialsRepository credRepo) {
        this.credRepo = credRepo;
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }
    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public GameRepository getGameRepo() {
        return gameRepo;
    }
    @Autowired
    public void setGameRepo(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    public TimeLineItemRepository getTliRepo() {
        return tliRepo;
    }
    @Autowired
    public void setTliRepo(TimeLineItemRepository tliRepo) {
        this.tliRepo = tliRepo;
    }

    public EntryRepository getEntryRepo() {
        return entryRepo;
    }
    @Autowired
    public void setEntryRepo(EntryRepository entryRepo) {
        this.entryRepo = entryRepo;
    }

    public PlayerCharacterRepository getPcRepo() {
        return pcRepo;
    }
    @Autowired
    public void setPcRepo(PlayerCharacterRepository pcRepo) {
        this.pcRepo = pcRepo;
    }

    public CollectedItemRepository getCiRepo() {
        return ciRepo;
    }
    @Autowired
    public void setCiRepo(CollectedItemRepository ciRepo) {
        this.ciRepo = ciRepo;
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
}