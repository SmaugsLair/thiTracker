package com.smaugslair.thitracker.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;

public abstract class AbstractMainView extends VerticalLayout implements BeforeEnterListener {

    //private final static Logger log = LoggerFactory.getLogger(AbstractMainView.class);
    private final TitleBar titleBar;

    protected AbstractMainView(TitleBar titleBar) {
        this.titleBar = titleBar;
        UI.getCurrent().addBeforeEnterListener(this);
        //log.info("constructed "+this.getClass().getSimpleName());
        titleBar.setTitle(getTitle());
        setHeightFull();

    }

    public abstract String getTitle();

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getNavigationTarget().equals(this.getClass())) {
            //log.info("Before Enter : " + beforeEnterEvent.getNavigationTarget());
            titleBar.setTitle(getTitle());
        }
    }

}
