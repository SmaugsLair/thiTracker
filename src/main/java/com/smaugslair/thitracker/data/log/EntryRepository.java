package com.smaugslair.thitracker.data.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByGameIdAndTypeEqualsOrderByIdDesc(Long gameId, EventType type);
}
