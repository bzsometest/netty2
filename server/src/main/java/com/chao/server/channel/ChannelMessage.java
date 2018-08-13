package com.chao.server.channel;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class ChannelMessage {
    /**
     * @param myMessage 发送的消息
     */
    public static void sendMessage(MyMessage myMessage) {
        Channel channelReceive = ChannelManager.getChannel(myMessage.getReceive_user());
        if (channelReceive == null || !channelReceive.isOpen()) {
            System.out.println("接收者用户通道关闭：" + myMessage.getReceive_user());
            return;
        }
        int netType = UserManager.getNetType(myMessage.getReceive_user());
        System.out.println("netType:" + netType);
        switch (netType) {
            case UserManager.NET_TYPE_CHAT:
                channelReceive.writeAndFlush(myMessage);
                break;
            case UserManager.NET_TYPE_WEBSOCKET:
                String json = new Gson().toJson(myMessage);
                channelReceive.writeAndFlush(new TextWebSocketFrame(json));
                break;
        }
    }

    /**
     * 验证并处理Message信息
     *
     * @param channel
     * @param myMessage
     */
    public static void handlerMessage(Channel channel, MyMessage myMessage) {
        //找到发送者信息
        String send_user = ChannelManager.getUsername(channel);

        String receive_user = myMessage.getReceive_user().trim();
        if (receive_user == null || receive_user.length() < 1) {
            System.out.println("无法找到接收者信息！");
            return;
        } else {
            System.out.println("发送者：" + send_user + "，接收者：" + receive_user);
            //正确的发送message信息
            myMessage.setSend_user(send_user);
            sendMessage(myMessage);
        }
    }

}
