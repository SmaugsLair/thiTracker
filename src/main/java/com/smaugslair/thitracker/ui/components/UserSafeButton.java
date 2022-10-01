package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.shared.Registration;

public class UserSafeButton extends Button {
    private ComponentEventListener<ClickEvent<Button>> doubleClickListener;
    private ComponentEventListener<ClickEvent<Button>> moreClickListener;

    public UserSafeButton(String text) {
        super(text);
    }

    public UserSafeButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
    }

    public UserSafeButton() {
        super();
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        return super.addClickListener(event -> {
            if(event.getClickCount() == 1) {
                listener.onComponentEvent(event);
            } else if (event.getClickCount() == 2 && doubleClickListener != null) {
                doubleClickListener.onComponentEvent(event);
            } else if (event.getClickCount() > 2 && moreClickListener != null) {
                moreClickListener.onComponentEvent(event);
            }
        });
    }

    public UserSafeButton doubleClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        this.doubleClickListener = listener;
        return this;
    }

    public UserSafeButton moreClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        this.moreClickListener = listener;
        return this;
    }
}

