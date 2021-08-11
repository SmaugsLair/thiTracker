package com.smaugslair.thitracker.services;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerRepository;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.data.powers.PowerSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
public class PowersCache {

    private PowerRepository powerRepo;
    private PowerSetRepository powerSetRepo;

    private final SortedSet<PowerSet> powerSets = new TreeSet<>();

    private final Map<String, Map<Integer, SortedSet<Power>>> powersMap = new HashMap<>();

    private final Map<String, PowerSet> powerSetMap = new HashMap<>();

    private final SortedSet<Power> powers = new TreeSet<>();

    public synchronized Map<String, Map<Integer, SortedSet<Power>>> getPowersMap() {
        if (powersMap.isEmpty()) {
            load();
        }
        return powersMap;
    }

    public synchronized Map<String, PowerSet> getPowerSetMap() {
        if (powerSetMap.isEmpty()) {
            load();
        }
        return powerSetMap;
    }

    public synchronized SortedSet<Power> getPowers() {
        if (powers.isEmpty()) {
            load();
        }
        return powers;
    }

    public synchronized SortedSet<PowerSet> getPowerSetList() {
        if (powerSets.isEmpty()) {
            load();
        }
        return powerSets;
    }

    @Autowired
    public void setPowerRepo(PowerRepository powerRepo) {
        this.powerRepo = powerRepo;
    }

    @Autowired
    public void setPowerSetRepo(PowerSetRepository powerSetRepo) {
        this.powerSetRepo = powerSetRepo;
    }


    public synchronized void load() {
        powers.clear();
        powerSets.clear();
        powerSetMap.clear();
        powersMap.clear();
        powerSetRepo.findAll().forEach(powerSet -> {
            powerSets.add(powerSet);
            powerSetMap.put(powerSet.getName(), powerSet);
            powersMap.put(powerSet.getName(), new HashMap<>());
        });
        powerRepo.findAll().forEach(power -> {
            power.fillPowerSetMap();
            powers.add(power);
            power.getPowerSetMap().forEach((psName, tier) -> {
                Map<Integer, SortedSet<Power>> tieredListMap = powersMap.get(psName);
                if (tieredListMap != null) {
                    SortedSet<Power> list = tieredListMap.computeIfAbsent(tier, k -> new TreeSet<>());
                    list.add(power);
                }
            });
        });
    }
}
