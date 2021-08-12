package com.smaugslair.thitracker.data.game;

import com.smaugslair.thitracker.data.ThiEntity;
import com.smaugslair.thitracker.data.atd.ActionTime;
import com.smaugslair.thitracker.data.atd.ActionTimeDefault;

import javax.persistence.*;
import java.util.*;

@Entity
public class TimeLineItem implements ThiEntity, Comparable<TimeLineItem> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Integer stun = 0;

    @Column
    private String color;

    @Column
    private Long pcId;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "tliId" )
    @MapKey(name = "name")
    Map<String,ActionTimeDelta> deltas = new HashMap<>();

    @Column(nullable = false)
    private Boolean hidden = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getStun() {
        return stun;
    }

    public void setStun(Integer stun) {
        this.stun = stun;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Map<String, ActionTimeDelta> getDeltas() {
        return deltas;
    }

    public void setDeltas(Map<String, ActionTimeDelta> deltas) {
        this.deltas = deltas;
    }

    public Long getPcId() {
        return pcId;
    }

    public void setPcId(Long pcId) {
        this.pcId = pcId;
    }

    //Transient properties

    @Transient
    private ActionTime actionTime;

    @Transient
    private List<ActionTime> actionTimes = new ArrayList<>();


    @Transient
    private Integer reactTime;

    public Integer getReactTime() {
        return reactTime;
    }

    public void setReactTime(Integer reactTime) {
        this.reactTime = reactTime;
    }

    public ActionTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(ActionTime actionTime) {
        this.actionTime = actionTime;
    }

    public List<ActionTime> getActionTimes() {
        return actionTimes;
    }


    public void initializeDeltas(List<ActionTimeDefault> defaults) {
        for (ActionTimeDefault atd : defaults) {
            ActionTimeDelta delta = new ActionTimeDelta();
            delta.setDelta(0);
            delta.setName(atd.getName());
            delta.setTimeLineItem(this);
            getDeltas().put(atd.getName(), delta);
        }
    }

    @Override
    public int compareTo(TimeLineItem o) {
        return -o.getTime().compareTo(getTime());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TimeLineItem.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("gameId=" + gameId)
                .add("name='" + name + "'")
                .add("time=" + time)
                .add("stun=" + stun)
                .add("color='" + color + "'")
                .add("pcId=" + pcId)
                .add("hidden=" + hidden)
                .add("actionTime=" + actionTime)
                .add("reactTime=" + reactTime)
                .toString();
    }

    @Override
    public TimeLineItem createEmptyObject() {
        TimeLineItem sample = new TimeLineItem();
        sample.setStun(null);
        sample.setHidden(null);
        sample.setDeltas(null);
        sample.actionTimes = null;
        return sample;
    }
}
