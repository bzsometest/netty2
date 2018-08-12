package com.chao.server.chatServer;

import com.chao.domian.MyMessage;
import com.chao.utils.ChannelManager;
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
public class ChatServerHandler extends SimpleChannelInboundHandler<MyMessage> { // (1)

    private final static String TAG = "ChatServerHandler:";
    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();

        System.out.println(TAG + "handlerAdded - " + incoming.remoteAddress() + " 加入");
        // Broadcast a message to multiple Channels
       // channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();

        // Broadcast a message to multiple Channels
       // channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        MyMessage myMessage = new MyMessage();
        myMessage.setMsg_text("aaaa");
        incoming.writeAndFlush(myMessage);
        System.out.println(TAG + "channelActive - " + incoming.remoteAddress() + " 在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println(TAG + "ChatClient:" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println(TAG + "ChatClient:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(TAG + "channelRead object");
        if (msg instanceof MyMessage) {
            System.out.println("MyMessage");
        }
        if (msg instanceof TextWebSocketFrame) {
            System.out.println("TextWebSocketFrame");
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage myMessage) throws Exception {
        System.out.println(TAG + "channelRead0:" + myMessage.getMsg_text());
    }
}