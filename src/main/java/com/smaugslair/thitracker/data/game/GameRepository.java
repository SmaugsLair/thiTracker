package com.smaugslair.thitracker.data.game;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
/*

    List<Game> findGamesByGameMasterId(Integer gmId);

    Optional<Game> findById(Long gameId);
*/

}
