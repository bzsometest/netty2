package com.chao.server.channel;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class ChannelManager {
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, Channel> map = new HashMap<>();

    public static void add(String username, Channel channel) {

        System.out.println("添加用户通道：" + username);
        map.put(username, channel);
    }

    public static Channel getChannel(String username) {
        return map.get(username);
    }

    public static String getUsername(Channel channel) {
        for (String key : map.keySet()) {
            if (channel == map.get(key)) {
                return key;
            }
        }
        return null;
    }

    public static void test() {
        System.out.println("map长度：" + map.size());
        if (map.get("chao2") == map.get("admin")) {
            System.out.println("同一个channel");
        } else {
            System.out.println("不是一个channel");
        }
    }
}
