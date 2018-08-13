package com.chao.domian;

import com.google.gson.Gson;

public class MessageManager {
    public static String getToString(MyMessage myMessage) {
        Gson gson = new Gson();
        return gson.toJson(myMessage);
    }

    public static MyMessage getToMessage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MyMessage.class);
    }
}
