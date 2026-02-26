package com.smaugslair.thitracker.data.pc;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//@Cacheable(value = "playerCharacters")
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {

    @NotNull
    Optional<PlayerCharacter> findById(@NotNull Long id);

    List<PlayerCharacter> findAllByUserId(Integer userId);
    List<PlayerCharacter> findAllByUserIdAndGameIdIsNull(Integer userId);
    List<PlayerCharacter> findAllByGameId(Long gameId);
}
