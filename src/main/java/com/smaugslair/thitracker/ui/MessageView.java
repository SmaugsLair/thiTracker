package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.Message;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.security.SecurityUtils;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Messages")
@Route(value = "messages", layout = MainView.class)
public class MessageView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(HeroView.class);

    //private final SessionService sessionService;

    public MessageView(SessionService sessionService) {
        //this.sessionService = sessionService;
        sessionService.getTitleBar().setTitle("Messages");

        User user = SecurityUtils.getLoggedInUser(sessionService);

        UserSafeButton removeAllButton = new UserSafeButton("Remove all messages", buttonClickEvent -> {
            sessionService.getMessageRepository().deleteAllByUserId(user.getId());
            UI.getCurrent().getPage().reload();
        });
        add(removeAllButton);

        List<Message> messages = sessionService.getMessageRepository().findAllByUserId(user.getId());

        MessageList messageList = new MessageList();
        List<MessageListItem> messageListItems = new ArrayList<>();
        for (Message message : messages) {
            MessageListItem item = new MessageListItem();
            item.setText(message.getText());
            item.setUserName("THI System");
            messageListItems.add(item);
        }
        messageList.setItems(messageListItems);
        add(messageList);
    }
}
