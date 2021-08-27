package com.smaugslair.thitracker.data.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeLineItemRepository extends JpaRepository<TimeLineItem, Long> {

    List<TimeLineItem> findByGameId(Long gameId);
    List<TimeLineItem> findByPcId(Long pcId);
    void deleteAllByGameId(Long gameId);
    Optional<TimeLineItem> findById(Long id);

}
