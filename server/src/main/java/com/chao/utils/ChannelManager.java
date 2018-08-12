package com.chao.utils;

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
        map.put(username, channel);
    }

    public static void sendToUser(String username, MyMessage myMessage) {
        Channel channel = map.get(username);
        switch (UserManager.getNetType(username)) {
            case UserManager.NET_TYPE_CHAT:
                channel.writeAndFlush(myMessage);
                break;
            case UserManager.NET_TYPE_WEBSOCKET:
                String json = new Gson().toJson(myMessage);
                channel.writeAndFlush(new TextWebSocketFrame(json));
                break;
        }
    }
}
