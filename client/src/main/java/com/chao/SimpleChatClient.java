package com.chao;

import com.chao.domian.MyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * 简单聊天服务器-客户端
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleChatClient {

    public static void main(String[] args) throws Exception {
        new SimpleChatClient("localhost", 9902).run();
    }

    private final String host;
    private final int port;

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)

                    //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new SimpleChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            MyMessage myMessage = new MyMessage();
            myMessage.setReceive_user("chao");
            myMessage.setSend_user("admin");

            while (true) {
                myMessage.setMsg_text(in.readLine());
                channel.writeAndFlush(myMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

}
