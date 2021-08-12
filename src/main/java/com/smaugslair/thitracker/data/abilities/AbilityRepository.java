package com.smaugslair.thitracker.data.abilities;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Cacheable("abilities")
public interface AbilityRepository extends JpaRepository<Ability, Integer> {

    @Override
    List<Ability> findAll();
}
