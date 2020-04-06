package com.example.andorid.youandi.model;

public class UserModel {

    public String userName;
    public String userId;
    public String userPassword;

    public UserModel(String userName, String userId, String userPassword) {
        this.userName = userName;
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
