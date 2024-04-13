package com.smaugslair.thitracker.data.atd;

import jakarta.persistence.*;

@Entity
public class ActionTimeDefault {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    private Integer time;

    private boolean stunable;

    public ActionTimeDefault() {
    }

    public ActionTimeDefault(String name, Integer time, boolean stunable) {
        this.name = name;
        this.time = time;
        this.stunable = stunable;
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean isStunable() {
        return stunable;
    }

    public void setStunable(boolean stunable) {
        this.stunable = stunable;
    }

}
