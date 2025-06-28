package com.smaugslair.thitracker.ui.templates;

import com.smaugslair.thitracker.data.templates.Template;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateForm extends VerticalLayout {


    private static final Logger log = LoggerFactory.getLogger(TemplateForm.class);

    private Template template;

    final Binder<Template> binder = new Binder<>(Template.class);

    private final TextField name = new TextField();
    private final TextArea text  = new TextArea();

    public TemplateForm() {
        this.template = new Template();
        init();
    }


    private void init() {
        binder.bindInstanceFields(this);
        binder.readBean(template);

        name.setRequired(true);
        name.setMinLength(6);
        name.setMaxLength(20);
        name.setRequiredIndicatorVisible(true);
        name.addValueChangeListener(event -> template.setName(event.getValue()));
        name.setPlaceholder("Template Name");
        add(name);
        binder.forField(name)
                .asRequired("Required")
                .withValidator(name -> name.length() >= 6, "6 char min")
                .bind("name");

        text.setRequired(true);
        text.setMinLength(2);
        text.setRequiredIndicatorVisible(true);
        text.addValueChangeListener(event -> template.setText(event.getValue()));
        text.setPlaceholder("Template Text");
        text.setWidthFull();
        add(text);
        binder.forField(text)
                .asRequired()
                .withValidator(text -> text.length() >= 2, "2 char min")
                .bind("text");

    }

    public void setTemplate(Template template) {
        removeAll();
        this.template = template;
        init();
    }

    public Template getTemplate() {
        if (binder.validate().isOk()) {
            return template;
        }
        return null;
    }
}
