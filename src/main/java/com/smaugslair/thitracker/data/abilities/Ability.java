package com.smaugslair.thitracker.data.abilities;

import javax.persistence.*;

@Entity
public class Ability implements Comparable<Ability>{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private Integer baseValue;

    @Column(nullable = false)
    private Integer sortOrder;

    public Ability() {
    }

    public Ability(String name, Integer baseValue, Integer sortOrder) {
        this.name = name;
        this.baseValue = baseValue;
        this.sortOrder = sortOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Integer baseValue) {
        this.baseValue = baseValue;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int compareTo(Ability o) {
        return sortOrder.compareTo(o.sortOrder);
    }
}
