package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.shared.Registration;

public class UserSafeNativeButton extends NativeButton {
    private ComponentEventListener<ClickEvent<NativeButton>> doubleClickListener;
    private ComponentEventListener<ClickEvent<NativeButton>> moreClickListener;

    public UserSafeNativeButton(String text) {
        super(text);
    }

    public UserSafeNativeButton(String text, ComponentEventListener<ClickEvent<NativeButton>> clickListener) {
        super(text, clickListener);
    }

    public UserSafeNativeButton() {
        super();
    }

    @Override
    public Registration addClickListener(ComponentEventListener<ClickEvent<NativeButton>> listener) {
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

    public UserSafeNativeButton doubleClickListener(ComponentEventListener<ClickEvent<NativeButton>> listener) {
        this.doubleClickListener = listener;
        return this;
    }

    public UserSafeNativeButton moreClickListener(ComponentEventListener<ClickEvent<NativeButton>> listener) {
        this.moreClickListener = listener;
        return this;
    }
}

