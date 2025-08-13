package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.game.TimeLineItem;
import com.smaugslair.thitracker.data.game.TimeLineItemRepository;
import com.smaugslair.thitracker.data.pc.HeroPowerRepository;
import com.smaugslair.thitracker.data.pc.HeroPowerSetRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.WelcomeView;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.components.events.AppEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@PageTitle("Hero Deletion")
@Route(value = "herodeletion", layout = MainView.class)
@UIScope
public class HeroDeletionView extends AbstractHeroView {

    private final TimeLineItemRepository timeLineItemRepository;
    private final HeroPowerSetRepository heroPowerSetRepository;
    private final HeroPowerRepository heroPowerRepository;
    private final PlayerCharacterRepository playerCharacterRepository;

    protected HeroDeletionView(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository, TitleBar titleBar, TimeLineItemRepository timeLineItemRepository, HeroPowerSetRepository heroPowerSetRepository, HeroPowerRepository heroPowerRepository, PlayerCharacterRepository playerCharacterRepository1) {
        super(uiService, gameRepository, playerCharacterRepository, titleBar);
        this.timeLineItemRepository = timeLineItemRepository;
        this.heroPowerSetRepository = heroPowerSetRepository;
        this.heroPowerRepository = heroPowerRepository;
        this.playerCharacterRepository = playerCharacterRepository1;
    }

    @Override
    protected void init() {
        titleBar.setTitle("Deletion of " +hero.getName());

        ConfirmDialog deleteDialog = new ConfirmDialog("Are you sure you want to delete the character "+hero.getName()+"?");
        Button confirmButton = new UserSafeButton("Delete", event -> {
            List<TimeLineItem> items = timeLineItemRepository.findByPcId(hero.getId());
            items.forEach(item -> {
                item.setPcId(null);
                item.setName(hero.getName());
            });
            timeLineItemRepository.saveAll(items);
            heroPowerRepository.deleteAll(heroPowerRepository.findAllByPlayerCharacter(hero));
            heroPowerSetRepository.deleteAll(heroPowerSetRepository.findAllByPlayerCharacter(hero));
            playerCharacterRepository.delete(hero);
            uiService.fireEvent(new AppEvent(AppEvent.AppEventType.MenuChange, null));
            //getEventBus().fireEvent(new HeroEvent(this, false));
            deleteDialog.close();

            uiService.fireEvent(new AppEvent(AppEvent.AppEventType.MenuChange, hero.getId()));
            UI.getCurrent().navigate(WelcomeView.class);
            titleBar.setTitle("");
            //refresh();
        });
        deleteDialog.setConfirmButton(confirmButton);
        deleteDialog.open();
    }

}
