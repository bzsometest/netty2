package com.chao.server.channel;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChannelMessage {
    private final static Logger logger = LoggerFactory.getLogger(ChannelMessage.class);
    /**
     * @param myMessage 发送的消息
     */
    public static void sendMessage(MyMessage myMessage) {
        Channel channelReceive = ChannelManager.getChannel(myMessage.getReceive_user());
        if (channelReceive == null || !channelReceive.isOpen()) {
            logger.info("接收者用户通道关闭：{}",myMessage.getReceive_user());
            return;
        }
        int netType = UserManager.getNetType(myMessage.getReceive_user());
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
            logger.info("无法从channel中找到接收者信息！");
            return;
        } else {
            logger.info("发送者：{}，接收者：{}" ,send_user,receive_user);
            //正确的发送message信息
            myMessage.setSend_user(send_user);
            sendMessage(myMessage);
        }
    }

}
