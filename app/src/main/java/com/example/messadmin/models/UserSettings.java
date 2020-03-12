package com.example.messadmin.models;

public class UserSettings {

    private User user;
    private UserAccountSetting userAccountSetting;

    public UserSettings(User user, UserAccountSetting userAccountSetting) {
        this.user = user;
        this.userAccountSetting = userAccountSetting;
    }

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", userAccountSetting=" + userAccountSetting +
                '}';
    }
}
