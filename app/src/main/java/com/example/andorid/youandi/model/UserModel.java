package com.example.andorid.youandi.model;

public class UserModel {

    public String userName;
    public String userId;
    public String uid;

    public UserModel() {

    }

    public UserModel(String userName, String userId, String uid) {
        this.userName = userName;
        this.userId = userId;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
