package com.smaugslair.thitracker.data.log;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
