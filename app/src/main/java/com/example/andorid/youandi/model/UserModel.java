package com.example.andorid.youandi.model;

public class UserModel {

    public String userName;
    public String userId;

    public UserModel() {

    }

    public UserModel(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
