package com.smaugslair.thitracker.data.user;


import com.vaadin.flow.data.provider.ListDataProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserFilter {

    private final ListDataProvider<User> dataProvider;
    private final static Logger log = LoggerFactory.getLogger(UserFilter.class);

    private String name = "";

    private String email = "";

    private String displayName = "";

    private String admin = "";

    public UserFilter(ListDataProvider<User> dataProvider) {
        this.dataProvider = dataProvider;
        dataProvider.setFilter(this::test);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        dataProvider.refreshAll();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        dataProvider.refreshAll();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        dataProvider.refreshAll();
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
        dataProvider.refreshAll();
    }


    public boolean test(User user) {
        if (name.length() > 0 && !StringUtils.containsIgnoreCase(user.getName(), name)) {
            return false;
        }
        if (email.length() > 0 && !StringUtils.containsIgnoreCase(user.getEmail(), email)) {
            return false;
        }
        if (displayName.length() > 0 && !StringUtils.containsIgnoreCase(user.getDisplayName(), displayName)) {
            return false;
        }
        if (admin.length() > 0 && !StringUtils.containsIgnoreCase(
                String.valueOf(user.isAdmin()), admin)) {
            return false;
        }
        return true;
    }
}
