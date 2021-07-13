package com.smaugslair.thitracker.data.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TimeLineItemRepository extends JpaRepository<TimeLineItem, Long> {

    public List<TimeLineItem> findByGameIdOrderByTime(Long gameId);

    public List<TimeLineItem> findByGameIdAndHiddenFalseOrderByTime(Long gamedId);

    public List<TimeLineItem> findAllByPcId(Long pcId);

    @Transactional
    public void deleteAllByGameId(Long gameId);
}
