package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserFilter;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.components.FilterField;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.users.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Admin Page")
@Route(value = "useradmin", layout = MainView.class)
@CssImport(value = "./styles/minPadding.css", themeFor = "vaadin-grid")
public class UsersView extends VerticalLayout {

    private final UserRepository userRepository;
    private final Dialog editUserDialog;
    private final UserForm userForm;


    public UsersView(SessionService sessionService, UserRepository userRepository) {

        sessionService.getTitleBar().setTitle("Manage Users");
        this.userRepository = userRepository;

        editUserDialog = new Dialog();
        editUserDialog.setWidth("500px");
        userForm = new UserForm();
        editUserDialog.add(userForm);
        Button saveNewUser = new UserSafeButton("Apply", event -> {
            User user = userForm.getUser();
            if (user != null) {
                userRepository.save(user);
                editUserDialog.close();
                removeAll();
                init();
            }
        });
        editUserDialog.add(saveNewUser);
        init();
    }

    private void init() {

        Grid<User> userGrid = new Grid<>();
        userGrid.setThemeName("min-padding");
        ListDataProvider<User> dataProvider = new ListDataProvider<>(userRepository.findAll());
        userGrid.setDataProvider(dataProvider);

        UserFilter filterObject = new UserFilter(dataProvider);
        dataProvider.setFilter(filterObject::test);


        //Grid.Column<User> nameColumn = userGrid.addColumn(User::getName).setHeader("Name");
        Grid.Column<User> dnColumn = userGrid.addColumn(User::getDisplayName).setHeader("Display Name");
        Grid.Column<User> emailColumn = userGrid.addColumn(User::getEmail).setHeader("Email");
        Grid.Column<User> adminColumn = userGrid.addColumn(User::isAdmin).setHeader("Admin");

        HeaderRow filterRow = userGrid.appendHeaderRow();
        //filterRow.getCell(nameColumn).setComponent(new FilterField(filterObject::setName));
        filterRow.getCell(dnColumn).setComponent(new FilterField(filterObject::setDisplayName));
        filterRow.getCell(emailColumn).setComponent(new FilterField(filterObject::setEmail));
        filterRow.getCell(adminColumn).setComponent(new FilterField(filterObject::setAdmin, "50px"));

        add(userGrid);
        GridContextMenu<User> contextMenu = new GridContextMenu<>(userGrid);
        GridMenuItem<User> editMenu = contextMenu.addItem("Edit", event -> {
            if (event.getItem().isPresent()) {
                userForm.setUser(event.getItem().get());
                editUserDialog.open();
            }
        });

    }

}
