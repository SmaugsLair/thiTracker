package com.smaugslair.thitracker.data.atd;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Cacheable(value = "atds")
public interface AtdRepository extends JpaRepository<ActionTimeDefault, Integer> {

    @Override
    List<ActionTimeDefault> findAll();
}
