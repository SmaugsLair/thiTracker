package com.smaugslair.thitracker.ui.friends;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.Friendship;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;


public class FriendsActions extends VerticalLayout {

    private final Friendship friendship;

    public FriendsActions(Friendship friendship, List<PlayerCharacter> pcs, FriendsList parent) {
        this.friendship = friendship;
        if (friendship.getAccepted()) {
            Button deleteButton = new Button("Remove");
            add(new Button("Remove", event -> parent.delete(friendship)));
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
                pcRow.add(icon, new Label(playerCharacter.getName()));
                add(pcRow);
            });
        }
        else {
            HorizontalLayout buttonRow = new HorizontalLayout();
            if (SecurityUtils.getLoggedInUser().equals(friendship.getUser())) {
                buttonRow.add(new Label("Waiting..."));
                buttonRow.add(new Button("Cancel", event -> parent.delete(friendship)));
            }
            else {
                buttonRow.add(new Button("Confirm", event -> parent.accept(friendship)));
                buttonRow.add(new Button("Reject", event -> parent.delete(friendship)));
            }
            add(buttonRow);

        }
    }
}
