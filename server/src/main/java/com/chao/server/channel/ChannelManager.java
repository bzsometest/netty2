package com.chao.server.channel;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelManager {
    private final static Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    private static Map<String, Channel> map = new HashMap<>();

    public static void add(String username, Channel channel) {
        logger.info("添加用户通道: {}", username);
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
}
