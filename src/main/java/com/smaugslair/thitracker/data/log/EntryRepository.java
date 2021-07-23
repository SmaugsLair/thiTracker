package com.smaugslair.thitracker.data.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByGameIdAndTypeEqualsOrderByIdDesc(Long gameId, EventType type);

    @Transactional
    void deleteAllByGameIdAndType(Long gameId, EventType type);

    @Transactional
    void deleteAllByGameId(Long gameId);
}
