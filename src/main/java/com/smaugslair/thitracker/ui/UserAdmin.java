package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.user.User;
import com.smaugslair.thitracker.data.user.UserFilter;
import com.smaugslair.thitracker.data.user.UserRepository;
import com.smaugslair.thitracker.ui.components.FilterField;
import com.smaugslair.thitracker.ui.users.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Admin Page")
@Route(value = "useradmin", layout = MainView.class)
public class UserAdmin extends VerticalLayout {

    private final UserRepository userRepository;
    private final Dialog editUserDialog;
    private final UserForm userForm;


    public UserAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;

        editUserDialog = new Dialog();
        editUserDialog.setWidth("500px");
        userForm = new UserForm();
        editUserDialog.add(userForm);
        Button saveNewUser = new Button("Apply");
        saveNewUser.addClickListener(event -> {
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
        ListDataProvider<User> dataProvider = new ListDataProvider<>(userRepository.findAll());
        userGrid.setDataProvider(dataProvider);

        UserFilter filterObject = new UserFilter();
        dataProvider.setFilter(user -> filterObject.test(user));

        Grid.Column<User> nameColumn = userGrid.addColumn(User::getName).setHeader("Name");
        Grid.Column<User> dnColumn = userGrid.addColumn(User::getDisplayName).setHeader("Display Name");
        Grid.Column<User> emailColumn = userGrid.addColumn(User::getEmail).setHeader("Email");
        Grid.Column<User> adminColumn = userGrid.addColumn(User::isAdmin).setHeader("Admin");

        HeaderRow filterRow = userGrid.appendHeaderRow();

        FilterField nameFilterField = new FilterField();
        nameFilterField.addValueChangeListener(event -> {
            filterObject.setName(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(nameColumn).setComponent(nameFilterField);

        FilterField dnFilterField = new FilterField();
        dnFilterField.addValueChangeListener(event -> {
            filterObject.setDisplayName(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(dnColumn).setComponent(dnFilterField);

        FilterField emailFilterField = new FilterField();
        emailFilterField.addValueChangeListener(event -> {
            filterObject.setEmail(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(emailColumn).setComponent(emailFilterField);

        FilterField adminFilterField = new FilterField();
        adminFilterField.addValueChangeListener(event -> {
            filterObject.setAdmin(event.getValue());
            dataProvider.refreshAll();
        });
        filterRow.getCell(adminColumn).setComponent(adminFilterField);

        add(userGrid);
        GridContextMenu<User> contextMenu = new GridContextMenu<>(userGrid);
        GridMenuItem<User> editMenu = contextMenu.addItem("Edit", event -> {
            userForm.setUser(event.getItem().get());
            editUserDialog.open();
        });

    }

}
