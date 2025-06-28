package com.smaugslair.thitracker.ui.templates;

import com.smaugslair.thitracker.data.templates.Template;
import com.smaugslair.thitracker.data.templates.TemplateRepository;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.MainView;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Template")
@Route(value = "template", layout = MainView.class)
public class TemplateView extends VerticalLayout {

    private final TemplateRepository templateRepository;
    private final Dialog editTemplateDialog;
    private final TemplateForm templateForm;

    public TemplateView(SessionService sessionService, TemplateRepository templateRepository) {
        sessionService.getTitleBar().setTitle("Manage templates");
        this.templateRepository = templateRepository;

        editTemplateDialog = new Dialog();
        editTemplateDialog.setWidthFull();
        templateForm = new TemplateForm();
        editTemplateDialog.add(templateForm);
        Button saveTemplate = new UserSafeButton("Apply", event -> {
            Template template = templateForm.getTemplate();
            if (template != null) {
                templateRepository.save(template);
                editTemplateDialog.close();
                removeAll();
                init();
            }
        });
        editTemplateDialog.add(saveTemplate);
        init();
    }

    public void init() {

        Grid<Template> templateGrid = new Grid<>();
        templateGrid.setThemeName("min-padding");
        ListDataProvider<Template> dataProvider = new ListDataProvider<>(templateRepository.findAll());
        templateGrid.setDataProvider(dataProvider);

        //UserFilter filterObject = new UserFilter(dataProvider);
       // dataProvider.setFilter(filterObject::test);


        //Grid.Column<User> nameColumn = userGrid.addColumn(User::getName).setHeader("Name");
        Grid.Column<Template> nameColumn = templateGrid.addColumn(Template::getName).setHeader("Name");
        Grid.Column<Template> textColumn = templateGrid.addColumn(Template::getText).setHeader("Text");
        //Grid.Column<User> emailColumn = templateGrid.addColumn(User::getEmail).setHeader("Email");
        //Grid.Column<User> adminColumn = templateGrid.addColumn(User::isAdmin).setHeader("Admin");

        //HeaderRow filterRow = templateGrid.appendHeaderRow();
        //filterRow.getCell(nameColumn).setComponent(new FilterField(filterObject::setName));
        //filterRow.getCell(dnColumn).setComponent(new FilterField(filterObject::setDisplayName));
        //filterRow.getCell(emailColumn).setComponent(new FilterField(filterObject::setEmail));
        //filterRow.getCell(adminColumn).setComponent(new FilterField(filterObject::setAdmin, "50px"));

        add(templateGrid);
        GridContextMenu<Template> contextMenu = new GridContextMenu<>(templateGrid);
        GridMenuItem<Template> editMenu = contextMenu.addItem("Edit", event -> {
            if (event.getItem().isPresent()) {
                templateForm.setTemplate(event.getItem().get());
                editTemplateDialog.open();
            }
        });

        Button newTemplateButton = new UserSafeButton("Create new template", event -> {
            Template template = new Template();
            templateForm.setTemplate(template);
            editTemplateDialog.open();
        });
        add(newTemplateButton);
    }
}
