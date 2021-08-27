package com.smaugslair.thitracker.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Cacheable("collectedItems")
public interface CollectedItemRepository extends JpaRepository<CollectedItem, Long> {

    //@Cacheable(value="collectedItems")
    List<CollectedItem> findAllByGmId(Integer gmId);

    @Override
    //@CacheEvict(value = "collectedItems", allEntries = true)
    void delete(CollectedItem entity);

    @Override
    //@CacheEvict(value = "collectedItems", allEntries = true)
    CollectedItem save(CollectedItem collectedItem);
}
