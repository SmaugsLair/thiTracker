package com.smaugslair.thitracker.data.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByUserId(Integer userid);

    @Transactional
    void deleteAllByUserId(Integer userid);
}
