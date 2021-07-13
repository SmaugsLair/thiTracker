package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.data.user.CredentialsRepository;
import com.smaugslair.thitracker.data.user.FriendshipRepository;
import com.smaugslair.thitracker.data.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

    private AtdRepository atdRepo;
    private CredentialsRepository credRepo;
    private UserRepository userRepo;
    //private SessionService sessionService;
    private GameRepository gameRepo;
    private TimeLineItemRepository tliRepo;
    private EntryRepository entryRepo;
    private PlayerCharacterRepository pcRepo;
    private CollectedItemRepository ciRepo;
    private FriendshipRepository friendsRepo;


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
/*

    public SessionService getSessionService() {
        return sessionService;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
*/

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
}
