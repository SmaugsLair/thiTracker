package com.smaugslair.thitracker.data.user;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CollectedItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer gmId;

    @Column()
    private String color;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "ciId" )
    Set<CollectedDelta> deltas = new HashSet<>();

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

    public Integer getGmId() {
        return gmId;
    }

    public void setGmId(Integer gmId) {
        this.gmId = gmId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<CollectedDelta> getDeltas() {
        return deltas;
    }

    public void setDeltas(Set<CollectedDelta> deltas) {
        this.deltas = deltas;
    }
/*
    @Override
    public CollectedItem createEmptyObject() {
        CollectedItem item = new CollectedItem();
        item.deltas = null;
        return item;
    }*/
}
