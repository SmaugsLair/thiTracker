package com.smaugslair.thitracker.data.game;

import javax.persistence.*;

@Entity
public class ActionTimeDelta {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer delta;

    @ManyToOne
    @JoinColumn(name = "tliId")
    TimeLineItem timeLineItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }

    public TimeLineItem getTimeLineItem() {
        return timeLineItem;
    }

    public void setTimeLineItem(TimeLineItem timeLineItem) {
        this.timeLineItem = timeLineItem;
    }

}
