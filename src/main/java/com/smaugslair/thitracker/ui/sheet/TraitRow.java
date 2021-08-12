package com.smaugslair.thitracker.ui.sheet;

import com.smaugslair.thitracker.data.pc.TraitType;
import com.vaadin.flow.component.Component;

public interface TraitRow  {

    public Component getLabel();

    public Component getComponent();

    public String getColor();

    public String getLabelValue();

    public TraitType getTraitType();
}
