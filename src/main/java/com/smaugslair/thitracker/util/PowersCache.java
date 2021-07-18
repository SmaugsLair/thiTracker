package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerRepository;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.data.powers.PowerSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowersCache {

    private PowerRepository powerRepo;
    private PowerSetRepository powerSetRepo;

    private List<PowerSet> powerSetList = new ArrayList<>();

    private Map<String, Map<Integer, List<Power>>> powersMap = new HashMap<>();

    private Map<String, PowerSet> powerSetMap = new HashMap<>();

    private List<Power> powerList = new ArrayList<>();

    public synchronized Map<String, Map<Integer, List<Power>>> getPowersMap() {
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

    public synchronized List<Power> getPowerList() {
        if (powerList.isEmpty()) {
            load();
        }
        return powerList;
    }

    public synchronized List<PowerSet> getPowerSetList() {
        if (powerSetList.isEmpty()) {
            load();
        }
        return powerSetList;
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
        powerList.clear();
        powerSetMap.clear();
        powersMap.clear();
        powerSetRepo.findAll(Sort.by(Sort.Direction.ASC, "name")).forEach(powerSet -> {
            powerSetList.add(powerSet);
            powerSetMap.put(powerSet.getName(), powerSet);
            powersMap.put(powerSet.getName(), new HashMap<Integer, List<Power>>());
        });
        powerRepo.findAll().forEach(power -> {
            power.fillPowerSetMap();
            powerList.add(power);
            power.getPowerSetMap().forEach((psName, tier) -> {
                Map<Integer, List<Power>> tieredListMap = powersMap.get(psName);
                if (tieredListMap != null) {
                    List<Power> list = tieredListMap.get(tier);
                    if (list == null) {
                        list = new ArrayList<>();
                        tieredListMap.put(tier, list);
                    }
                    list.add(power);
                }
            });
        });
    }
}
