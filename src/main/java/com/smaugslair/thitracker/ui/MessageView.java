package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.Message;
import com.smaugslair.thitracker.data.user.MessageRepository;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Messages")
@Route(value = "messages", layout = MainView.class)
@UIScope
@Component
public class MessageView extends AbstractMainView {

    private static final Logger log = LoggerFactory.getLogger(HeroView.class);


    protected final SessionService sessionService;
    protected final MessageRepository messageRepository;

    public MessageView(SessionService sessionService, TitleBar titleBar, MessageRepository messageRepository) {
        super(titleBar);
        this.sessionService = sessionService;
        this.messageRepository = messageRepository;
        init();
    }

    public void init() {

        User user = sessionService.getLoggedInUser();

        UserSafeButton removeAllButton = new UserSafeButton("Remove all messages", buttonClickEvent -> {
            messageRepository.deleteAllByUserId(user.getId());
            UI.getCurrent().getPage().reload();
        });
        add(removeAllButton);

        List<Message> messages = messageRepository.findAllByUserId(user.getId());

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

    @Override
    public String getTitle() {
        return "Messages";
    }
}
