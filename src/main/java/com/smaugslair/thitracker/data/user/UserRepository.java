package com.smaugslair.thitracker.data.user;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    User findUserByName(String name);

    @Override
    @Cacheable("users")
    Optional<User> findById(Integer id);

    @Override
    @CacheEvict(value = "users", allEntries = true)
    User save(User user);
}
