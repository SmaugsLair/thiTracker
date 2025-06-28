package com.smaugslair.thitracker.data.pc;

import com.smaugslair.thitracker.data.powers.Power;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroPowerRepository extends JpaRepository<HeroPower, Long> {

    List<HeroPower> findAllByPlayerCharacter(PlayerCharacter playerCharacter);

    List<HeroPower> findAllByPower(Power power);
}
