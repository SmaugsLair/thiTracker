package com.smaugslair.thitracker.data.pc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Cacheable(value = "playerCharacters")
public interface PCNoteRepository extends JpaRepository<PCNote, Long> {


    List<PCNote> findAllByPlayerCharacter(PlayerCharacter playerCharacter);

    void deleteAllByPlayerCharacter(PlayerCharacter playerCharacter);
}