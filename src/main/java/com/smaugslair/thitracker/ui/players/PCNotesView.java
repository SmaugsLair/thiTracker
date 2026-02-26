package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.game.GameRepository;
import com.smaugslair.thitracker.data.pc.PCNote;
import com.smaugslair.thitracker.data.pc.PCNoteRepository;
import com.smaugslair.thitracker.data.pc.PlayerCharacterRepository;
import com.smaugslair.thitracker.services.UIService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@PermitAll
@Route(value = "pcnotes", layout = MainView.class)
@RouteScope
@Component
public class PCNotesView extends AbstractHeroView {

    private static final Logger log = LoggerFactory.getLogger(PrintablePowers.class);

    private final PCNoteRepository pcNoteRepository;

    protected PCNotesView(UIService uiService, GameRepository gameRepository, PlayerCharacterRepository playerCharacterRepository, TitleBar titleBar,
                          PCNoteRepository pcNoteRepository) {
        super(uiService, gameRepository, playerCharacterRepository, titleBar);
        this.pcNoteRepository = pcNoteRepository;
        setWidthFull();
    }

    protected void init() {
        titleBar.setTitle("Notes for "+hero.getName());
        removeAll();
        if (hero == null) {
            add("No hero found");
            return;
        }
        add(new UserSafeButton("Add new note", buttonClickEvent -> {addNewNote();}));

        List<PCNote> notes = pcNoteRepository.findAllByPlayerCharacter(hero);
        for (PCNote note : notes) {
            add(new PCNoteField(note, this));
        }

    }

    private void addNewNote() {
        PCNote note = new PCNote();
        note.setText("");
        note.setPlayerCharacter(hero);
        add(new PCNoteField(note, this));
    }


    public void delete(PCNote note) {
        if (note.getId() != null) {
            pcNoteRepository.delete(note);
        }
        init();
    }

    public void updateNote(PCNote note) {
        pcNoteRepository.save(note);
    }
}
