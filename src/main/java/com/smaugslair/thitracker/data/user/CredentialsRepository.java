package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {

    Credentials findByUserId(Integer userId);
}
