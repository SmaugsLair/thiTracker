package com.smaugslair.thitracker.data.user;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Cacheable("friends")
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Cacheable(value = "friends")
    List<Friendship> findAllByUserOrFriend(User user, User friend);

    @Cacheable(value = "friends")
    Optional<Friendship> findByUserAndFriend(User user, User friend);

    @Override
    @CacheEvict(value = "friends", allEntries = true)
    Friendship save(Friendship friendship);

    @Override
    @CacheEvict(value = "friends", allEntries = true)
    void delete(Friendship entity);
}
