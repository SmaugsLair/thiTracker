package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByName(String name);
    Optional<User> findUserByNameAndFriendCode(String name, String friendCode);
    Optional<User> findUserByEmail(String email);

}
