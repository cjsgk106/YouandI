package com.example.andorid.youandi.model;

public class UserModel {

    public String userName;
    public String userId;
    public String uid;
    public String partnerEmail;

    public UserModel() {

    }

    public UserModel(String userName, String userId, String uid, String partnerEmail) {
        this.userName = userName;
        this.userId = userId;
        this.uid = uid;
        this.partnerEmail = partnerEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
