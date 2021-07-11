package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectedItemRepository extends JpaRepository<CollectedItem, Long> {

    List<CollectedItem> findAllByGmId(Integer gmId);

}
