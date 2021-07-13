package com.smaugslair.thitracker.data.user;

import javax.persistence.*;

@Entity
public class Friendship {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name="userId")
    private User user;

    @OneToOne
    @JoinColumn(name="friendId")
    private User friend;

    private Boolean accepted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
