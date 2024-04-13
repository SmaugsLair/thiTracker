package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Objects;


public class FriendsActions extends VerticalLayout {


    public FriendsActions(Friendship friendship, List<PlayerCharacter> pcs, FriendsList parent) {

        if (friendship.getAccepted()) {
            Button deleteButton = new UserSafeButton("Remove");
            add(new UserSafeButton("Remove", event -> parent.delete(friendship)));
            pcs.forEach(playerCharacter -> {
                HorizontalLayout pcRow = new HorizontalLayout();
                Icon icon;
                if (playerCharacter.getGameId() != null) {
                    icon = VaadinIcon.SMILEY_O.create();
                }
                else {
                    icon = VaadinIcon.FROWN_O.create();
                }
                icon.setSize("16px");
                pcRow.add(icon, new Span(playerCharacter.getName()));
                add(pcRow);
            });
        }
        else {
            HorizontalLayout buttonRow = new HorizontalLayout();
            if (Objects.equals(SecurityUtils.getLoggedInUser(), friendship.getUser())) {
                buttonRow.add(new Span("Waiting..."));
                buttonRow.add(new UserSafeButton("Cancel", event -> parent.delete(friendship)));
            }
            else {
                buttonRow.add(new UserSafeButton("Confirm", event -> parent.accept(friendship)));
                buttonRow.add(new UserSafeButton("Reject", event -> parent.delete(friendship)));
            }
            add(buttonRow);

        }
    }
}
