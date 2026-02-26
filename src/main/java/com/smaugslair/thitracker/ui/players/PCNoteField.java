package com.smaugslair.thitracker.ui.players;

import com.smaugslair.thitracker.data.pc.PCNote;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class PCNoteField extends HorizontalLayout {
    
    private final TextArea textArea = new TextArea();
    private final PCNote note;
    private final PCNotesView parent;

    public PCNoteField(PCNote note, PCNotesView parent) {
        this.note = note;
        this.parent = parent;
        add(textArea);
        textArea.setValue(note.getText());
        textArea.setRequired(true);
        textArea.setMinLength(5);
        textArea.setMaxLength(255);
        textArea.setWidthFull();
        //textArea.setMinLength(1);
        textArea.setRequiredIndicatorVisible(true);
        textArea.setPlaceholder("Note - 5-255 characters");
        textArea.addValueChangeListener( event -> {
            note.setText(textArea.getValue());
            parent.updateNote(note);
        } );
        add(new UserSafeButton("Delete", buttonClickEvent -> {deleteNote();}));
        setWidthFull();
    }

    private void deleteNote() {
        parent.delete(note);
    }
}
