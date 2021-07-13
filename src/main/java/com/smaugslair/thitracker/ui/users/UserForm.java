package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.data.user.User;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserForm extends FormLayout {

    private static final Logger log = LoggerFactory.getLogger(UserForm.class);

    private User user;

    Binder<User> binder = new Binder<>(User.class);

    private TextField name = new TextField();
    private EmailField email = new EmailField();
    private TextField displayName = new TextField();
    private final boolean admin;
    private Checkbox adminBox = new Checkbox();

    public UserForm(User user) {
        this.user = user;
        admin = false;
        init();
    }

    public UserForm() {
        admin = true;
        init();
    }

    private void init() {
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

        if (user != null) {
            addFormItem(new Text(user.getFriendCode()), "Friend Code");
        }

        if (admin) {
            adminBox.addValueChangeListener(event -> user.setAdmin(event.getValue()));
            addFormItem(adminBox, "Admin");
            binder.forField(adminBox).bind("admin");
        }

    }

    public void setUser(User user) {
        removeAll();
        this.user = user;
        init();
    }

    public User getUser() {
        if (binder.validate().isOk()) {
            return user;
        }
        return null;
    }





}
