package com.chao.server.webServer;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 服务端 channel
 *
 * @author waylau.com
 * @date 2015-2-16
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> { // (1)

    private final static String TAG = "TextWebSocketHandler:";
    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();

        // Broadcast a message to multiple Channels
        System.out.println(TAG + "handlerAdded - " + incoming.remoteAddress() + " 加入");
        // ctx.channel().writeAndFlush(new TextWebSocketFrame("[SERVERc] - " + incoming.remoteAddress() + " 加入"));
        channels.add(incoming);

        //   incoming.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)

        Channel incoming = ctx.channel();
        // ctx.channel().writeAndFlush(new TextWebSocketFrame("[SERVERc] - " + incoming.remoteAddress() + " 加入"));
        System.out.println(TAG + "channelActive:" + incoming.remoteAddress() + "在线");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println(TAG + "channelRead0:消息" + textWebSocketFrame.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("[me] : " + textWebSocketFrame.text()));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println(TAG + "channelInactive" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();

        // Broadcast a message to multiple Channels
        // incoming.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println(TAG + "exceptionCaught" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}