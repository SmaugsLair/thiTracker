package com.smaugslair.thitracker.data.pc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroPowerSetRepository extends JpaRepository<HeroPowerSet, Long> {

    List<HeroPowerSet> findAllByPlayerCharacter(PlayerCharacter playerCharacter);
}
