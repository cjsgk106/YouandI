package com.example.andorid.youandi.model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, MessageModel> messages = new HashMap<>();

    public static class MessageModel {
        public String uid;
        public String message;
        public Object timestamp;
    }
}