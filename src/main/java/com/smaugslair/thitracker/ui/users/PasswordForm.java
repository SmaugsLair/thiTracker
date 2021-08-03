package com.smaugslair.thitracker.ui.users;

import com.smaugslair.thitracker.util.PasswordVerify;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;

public class PasswordForm extends FormLayout {

    final Binder<PasswordVerify> binder = new Binder<>(PasswordVerify.class);

    private final PasswordField password = new PasswordField();

    public PasswordForm() {
        binder.bindInstanceFields(this);
        PasswordVerify passwordVerify = new PasswordVerify();
        binder.readBean(passwordVerify);

        password.setRequired(true);
        password.setMinLength(8);
        password.setMaxLength(20);
        password.setRequiredIndicatorVisible(true);
        password.setRevealButtonVisible(true);
        addFormItem(password, "Password");
        binder.forField(password)
                .asRequired("Required")
                .withValidator(password -> password.length() >= 8, "8 char min")
                .bind("password");

        PasswordField verify = new PasswordField();
        verify.setRequired(true);
        verify.setMinLength(8);
        verify.setMaxLength(20);
        verify.setRequiredIndicatorVisible(true);
        verify.setRevealButtonVisible(true);
        addFormItem(verify, "Verify");

        binder.forField(verify)
                .withValidator(value -> value.equals(password.getValue()), "Mismatch")
                .bind("verify");

    }

    public String getValidPassword() {
        if (binder.validate().isOk()) {
            return password.getValue();
        }
        return null;
    }
}
