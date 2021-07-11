package com.smaugslair.thitracker.data.atd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AtdRepository extends JpaRepository<ActionTimeDefault, Integer> {

}
