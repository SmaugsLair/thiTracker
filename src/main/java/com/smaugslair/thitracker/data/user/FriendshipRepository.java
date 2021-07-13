package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    //List<Friendship> findAllByUserId(Integer userId);
    //List<Friendship> findAllByFriendId(Integer userId);

    List<Friendship> findAllByUserOrFriend(User user, User friend);

    Optional<Friendship> findByUserAndFriend(User user, User friend);

}
