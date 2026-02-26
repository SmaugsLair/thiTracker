package com.smaugslair.thitracker.data.game;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeLineItemRepository extends JpaRepository<TimeLineItem, Long> {

    List<TimeLineItem> findByGameId(Long gameId);
    List<TimeLineItem> findByPcId(Long pcId);

    @Transactional
    void deleteAllByGameId(Long gameId);/*
    @NotNull
    Optional<TimeLineItem> findById(@NotNull Long id);*/

}
