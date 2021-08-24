package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    User findUserByName(String name);

    Optional<User> findById(Integer id);

    User save(User user);
}
