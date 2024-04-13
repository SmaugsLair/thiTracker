package com.smaugslair.thitracker.data.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
public class PasswordReset {

    @Id
    @Column
    String hash;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date created;

    @Column
    Integer userId;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isExpired() {
        if (created == null) {
            return true;
        }
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        LocalDateTime resetTime = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return resetTime.isBefore(fiveMinutesAgo);
    }
}
