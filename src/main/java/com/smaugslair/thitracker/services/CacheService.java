package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.atd.AtdRepository;
import com.smaugslair.thitracker.data.game.Game;
import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.log.Entry;
import com.smaugslair.thitracker.data.log.EntryRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.data.user.CollectedItemRepository;
import com.smaugslair.thitracker.util.JPACache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CacheService is meant to hold repos that have frequent read/writes.
 */

@Component
public class CacheService {

    private JPACache<TimeLineItem, Long> tliCache;
    private JPACache<Game, Long> gameCache;
    private JPACache<PlayerCharacter, Long> pcCache;
    private JPACache<Entry, Long> entryCache;

    private AtdRepository atdRepo;
    private CollectedItemRepository ciRepo;


    public AtdRepository getAtdRepo() {
        return atdRepo;
    }
    @Autowired
    public void setAtdRepo(AtdRepository atdRepository) {
        this.atdRepo = atdRepository;
    }


    public JPACache<TimeLineItem, Long> getTliCache() {
        return tliCache;
    }
    @Autowired
    public void setTliRepo(TimeLineItemRepository tliRepo) {
        tliCache = new JPACache<>(new TimeLineItem(), tliRepo);
    }

    public JPACache<Game, Long> getGameCache() {
        return gameCache;
    }
    @Autowired
    public void setGameRepo(GameRepository gameRepo) {
        gameCache = new JPACache<>(new Game(), gameRepo);
    }

    @Autowired
    public void setCiRepo(CollectedItemRepository ciRepo) {
        this.ciRepo = ciRepo;
    }
    public CollectedItemRepository getCiRepo() {
        return ciRepo;
    }

    public JPACache<Entry, Long> getEntryCache() {
        return entryCache;
    }

    @Autowired
    public void setEntryRepo(EntryRepository entryRepo) {
        entryCache = new JPACache<>(new Entry(), entryRepo);
    }

    @Autowired
    public void setPcRepo(PlayerCharacterRepository pcRepo) {
        pcCache = new JPACache<>(new PlayerCharacter(), pcRepo);
    }
    public JPACache<PlayerCharacter, Long> getPcCache() {
        return pcCache;
    }
}
