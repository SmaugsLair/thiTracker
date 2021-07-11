package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserForm extends FormLayout {

    private static final Logger log = LoggerFactory.getLogger(UserForm.class);

    private final User user;

    Binder<User> binder = new Binder<>(User.class);

    private TextField name = new TextField();
    private EmailField email = new EmailField();
    private TextField displayName = new TextField();

    public UserForm(User user) {
        this.user = user;
        binder.bindInstanceFields(this);
        binder.readBean(user);

        name.setRequired(true);
        name.setMinLength(6);
        name.setMaxLength(20);
        name.setRequiredIndicatorVisible(true);
        name.addValueChangeListener(event -> {
            user.setName(event.getValue());
        });
        addFormItem(name, "Username");
        binder.forField(name)
                .asRequired("Required")
                .withValidator(name -> name.length() >= 6, "6 char min")
                .bind("name");

        email.setRequiredIndicatorVisible(true);
        email.addValueChangeListener(event -> user.setEmail(event.getValue()));
        addFormItem(email, "Email");
        binder.forField(email).withValidator(new EmailValidator("Invalid email")).bind("email");

        displayName.setRequired(true);
        name.setMinLength(6);
        name.setMaxLength(30);
        displayName.setRequiredIndicatorVisible(true);
        displayName.addValueChangeListener(event -> user.setDisplayName(event.getValue()));
        addFormItem(displayName, "Display Name");
        binder.forField(displayName)
                .asRequired("Required")
                .withValidator(value -> value.length() >= 6, "6 char min")
                .bind("displayName");

    }

    public User getUser() {
        if (binder.validate().isOk()) {
            log.info("getUser: "+user);
            return user;
        }
        return null;

    }





}
