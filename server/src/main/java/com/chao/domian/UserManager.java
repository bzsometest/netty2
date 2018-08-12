package com.chao.domian;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<String, String> users = new HashMap();
    private static Map<String, Integer> userNetType = new HashMap();
    public static final int NET_TYPE_WEBSOCKET = 0x1;

    public static final int NET_TYPE_CHAT = 0x2;

    static {
        users.put("chao", "123456");
        users.put("admin", "admin123");
        userNetType.put("chao", NET_TYPE_WEBSOCKET);
        userNetType.put("admin", NET_TYPE_CHAT);
    }

    public static void add(String username, String token) {
        users.put(username, token);
    }

    public static String getToken(String username) {
        return users.get(username);
    }

    public static String getUser(String token) {
        for (String key : users.keySet()) {
            if (users.get(key).equals(token)) {
                return key;
            }
        }
        return null;
    }

    public static int getNetType(String username) {
        return userNetType.get(username);
    }

}

