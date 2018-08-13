package com.chao.server;

import com.chao.server.chatServer.ChatServer;
import com.chao.server.webServer.WebSocketServer;

public class ChatAndWebSocket {
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                try {
                    new ChatServer(9902).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    new WebSocketServer(9901).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
