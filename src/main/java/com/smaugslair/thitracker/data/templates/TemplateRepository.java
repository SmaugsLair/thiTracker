package com.smaugslair.thitracker.data.templates;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TemplateRepository extends JpaRepository<Template, Integer> {


    Optional<Template> findByName(String name);

    @NotNull
    Optional<Template> findById(@NotNull Integer id);

    //Template save(Template template);
}
