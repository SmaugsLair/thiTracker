package com.smaugslair.thitracker.data.pc;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Cacheable(value = "playerCharacters")
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    List<PlayerCharacter> findAllByUserId(Integer userId);
    List<PlayerCharacter> findAllByUserIdAndGameIdIsNull(Integer userId);
    List<PlayerCharacter> findAllByGameId(Long gameId);
}
