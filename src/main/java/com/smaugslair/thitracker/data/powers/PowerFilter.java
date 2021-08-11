package com.smaugslair.thitracker.data.powers;


import com.vaadin.flow.data.provider.ListDataProvider;
import org.apache.commons.lang3.StringUtils;

public class PowerFilter {


    private final ListDataProvider<Power> dataProvider;

    private String name = "";

    private String powerTag = "";

    private String metaPower = "";

    private String tier = "";

    private String maxTaken = "";

    public PowerFilter(ListDataProvider<Power> dataProvider) {
        this.dataProvider = dataProvider;
        dataProvider.setFilter(this::test);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        dataProvider.refreshAll();
    }

    public String getPowerTag() {
        return powerTag;
    }

    public void setPowerTag(String powerTag) {
        this.powerTag = powerTag;
        dataProvider.refreshAll();
    }

    public String getMetaPower() {
        return metaPower;
    }

    public void setMetaPower(String metaPower) {
        this.metaPower = metaPower;
        dataProvider.refreshAll();
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
        dataProvider.refreshAll();
    }

    public String getMaxTaken() {
        return maxTaken;
    }

    public void setMaxTaken(String maxTaken) {
        this.maxTaken = maxTaken;
        dataProvider.refreshAll();
    }

    public boolean test(Power power) {
        if (name.length() > 0 && !StringUtils.containsIgnoreCase(power.getName(), name)) {
            return false;
        }
        if (powerTag.length() > 0 && !StringUtils.containsIgnoreCase(power.getPowerTag(), powerTag)) {
            return false;
        }
        if (tier.length() > 0 && !StringUtils.containsIgnoreCase(power.getTier().toString(), tier)) {
            return false;
        }
        if (maxTaken.length() > 0 && !StringUtils.containsIgnoreCase(power.getMaxTaken(), maxTaken)) {
            return false;
        }
        if (metaPower.length() > 0 && !StringUtils.containsIgnoreCase(
                String.valueOf(power.getMetaPower()), metaPower)) {
            return false;
        }
        return true;
    }
}
