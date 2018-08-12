package com.chao.server.channel;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class ChannelMessage {
    public static void sendToUser(String username, MyMessage myMessage) {
        Channel channel = ChannelManager.getChannel(username);
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

    public static void handlerMessage(Channel channel, MyMessage myMessage) {
        //找到发送者信息
        String username = ChannelManager.getUsername(channel);
        sendToUser(username, myMessage);
    }
}
