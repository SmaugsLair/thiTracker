package com.smaugslair.thitracker.data.user;

import javax.persistence.*;

@Entity
public class Credentials {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String encodedPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String password) {
        this.encodedPassword = password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", userId=" + userId +
                ", encodedPassword='" + encodedPassword + '\'' +
                '}';
    }
}
