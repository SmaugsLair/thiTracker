package com.smaugslair.thitracker.ui.games.tl;

import com.smaugslair.thitracker.data.pc.PlayerCharacter;
import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.ui.games.GMTimeLineView;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.IntegerField;

public class TokenDialog extends Dialog {

    private final GMTimeLineView gmTimeLineView;

    private final H4 nameHeader;
    private final IntegerField progression;
    private final IntegerField hero;
    private final IntegerField drama;

    private PlayerCharacter pc;

    public TokenDialog(GMTimeLineView gmTimeLineView) {
        this.gmTimeLineView = gmTimeLineView;

        FormLayout formLayout = new FormLayout();
        nameHeader = new H4();

        progression = new IntegerField();
        progression.setMin(0);
        progression.setHasControls(true);
        progression.addValueChangeListener(event -> {
            pc.setProgressionTokens(event.getValue());
            gmTimeLineView.updatePc(pc);
        });
        formLayout.addFormItem(progression, "Progression");

        hero = new IntegerField();
        hero.setMin(0);
        hero.setHasControls(true);
        hero.addValueChangeListener(event -> {
            pc.setHeroPoints(event.getValue());
            gmTimeLineView.updatePc(pc);
        });
        formLayout.addFormItem(hero, "Hero Points");

        drama = new IntegerField();
        drama.setMin(0);
        drama.setHasControls(true);
        drama.addValueChangeListener(event -> {
            pc.setDramaPoints(event.getValue());
            gmTimeLineView.updatePc(pc);
        });
        formLayout.addFormItem(drama, "Drama points");

        add(nameHeader, formLayout);
        setWidth("400px");
    }

    public void setPc(PlayerCharacter pc) {
        this.pc = pc;
        User user = gmTimeLineView.getUserCache().findOneById(pc.getUserId()).get();
        nameHeader.setText(pc.getCharacterAndPlayerName(user));
        progression.setValue(pc.getProgressionTokens());
        hero.setValue(pc.getHeroPoints());
        drama.setValue(pc.getDramaPoints());
    }
}
