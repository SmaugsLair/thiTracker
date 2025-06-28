package com.smaugslair.thitracker.data.templates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TemplateRepository extends JpaRepository<Template, Integer> {


    Optional<Template> findByName(String name);

    Optional<Template> findById(Integer id);

    Template save(Template template);
}
