package com.smaugslair.thitracker.data.user;


import org.apache.commons.lang3.StringUtils;

public class UserFilter {


    private String name = "";

    private String email = "";

    private String displayName = "";

    private String admin = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
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
