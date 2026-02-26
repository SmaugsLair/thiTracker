package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;

public interface TraitRow  {

    Component getLeft();

    Component getRight();

    String getColor();

    String getLeftValue();

    TraitType getTraitType();
}
