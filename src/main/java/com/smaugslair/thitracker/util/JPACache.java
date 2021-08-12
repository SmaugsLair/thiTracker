package com.smaugslair.thitracker.util;

import com.smaugslair.thitracker.data.ThiEntity;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class JPACache<T extends ThiEntity, ID extends Number> {

    //private static final Logger log = LoggerFactory.getLogger(JPACache.class);

    private final T starter;
    private final Map<ID, T> map = new HashMap<>();

    private final JpaRepository<T, ID> repo;

    public JPACache(T starter, JpaRepository<T, ID> repo) {
        this.repo = repo;
        this.starter = starter;
    }

    public Optional<T> findOneById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        //log.info("findOneById: id="+id+", class:"+starter.getClass().getSimpleName());
        if (map.containsKey(id)) {
            //log.info("found one cached");
            return Optional.of(map.get(id));
        }
        //log.info("didn't find, checking db");
        Optional<T> result = repo.findById(id);
        result.ifPresent(t -> map.put(id, t));
        //log.info("returning:"+result);
        return result;
    }


/*

This is not reliable as a call to this will load the cache with a single value, and a subsequent call to
findMany will only return the one without looking for others in the database

    public Optional<T> findOneByProperty(NameValue nameValue){
        //log.info("findOneByProperty: "+nameValue+", class:"+starter.getClass().getSimpleName());
        try {
            String property = nameValue.getName();
            Object value = nameValue.getValue();
            //log.info("Sampling: "+property+" "+value);
            for (T t : map.values()) {
                if (value.equals(PropertyUtils.getProperty(t, property))) {
                    //log.info("Found one cached: "+t);
                    return Optional.of(t);
                }
            }
            //log.info("didn't find, checking db");
            Optional<T> result = repo.findOne(createExample(nameValue));
            result.ifPresent(t -> map.put(t.getId(), t));
            //log.info("returning:"+result);
            return result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }*/


    public List<T> findManyByProperty(NameValue nameValue){
        //log.info("findManyByProperty: nameValue="+nameValue+", class:"+starter.getClass().getSimpleName());
        List<T> list = new ArrayList<>();
        try {
            String property = nameValue.getName();
            Object value = nameValue.getValue();
            //log.info("Sampling: "+property+" "+value);
            for (T t : map.values()) {
                if (value.equals(PropertyUtils.getProperty(t, property))) {
                    //log.info("Found one cached: "+t);
                    list.add(t);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            //log.info("didn't find any, checking db");
            list.addAll(repo.findAll(createExample(nameValue)));
            list.forEach(t -> map.put(t.getId(), t));
        }
        return list;

    }

    public T save(T item) {
        item = repo.save(item);
        map.put(item.getId(), item);
        return item;
    }

    public void delete(T item) {
        map.remove(item.getId());
        repo.delete(item);
    }

    public void saveAll(List<T> items) {
        List<T> saved = repo.saveAll(items);
        saved.forEach(t -> map.replace(t.getId(), t));
    }

    public void deleteAllByProperty(NameValue nameValue) {
        List<T> targets = findManyByProperty(nameValue);
        targets.forEach(t -> map.remove(t.getId()));
        repo.deleteAll(targets);
    }

    private Example<T> createExample(NameValue nameValue) {
        T sample = starter.createEmptyObject();
        try {
            PropertyUtils.setProperty(sample, nameValue.getName(), nameValue.getValue());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Example.of(sample);
    }

    public void clear() {
        map.clear();
    }
}
