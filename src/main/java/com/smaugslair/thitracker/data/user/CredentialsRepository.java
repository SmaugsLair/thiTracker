package com.smaugslair.thitracker.data.user;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {

    public Credentials findByUserId(Integer userId);
}
