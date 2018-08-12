package com.chao.server.chatServer;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.chao.domian.UserToken;
import com.chao.server.channel.ChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
        super.handlerAdded(ctx);
        Channel incoming = ctx.channel();
        System.out.println(TAG + "handlerAdded - " + incoming.remoteAddress() + " 加入");

        MyMessage myMessage = new MyMessage();
        myMessage.setMsg_text("欢迎加入a");
        ctx.channel().writeAndFlush(myMessage);

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
        super.channelActive(ctx);
        Channel incoming = ctx.channel();
        System.out.println(TAG + "channelActive - " + incoming.remoteAddress() + " 在线");


        MyMessage myMessage = new MyMessage();
        myMessage.setMsg_text("欢迎在线a");
        ctx.channel().writeAndFlush(myMessage);
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
        System.out.println(TAG + "channelRead Object");
        super.channelRead(ctx, msg);
        if (msg instanceof UserToken) {
            System.out.println("UserToken");
            UserToken userToken = (UserToken) msg;
            String username = UserManager.getUser(userToken.getToken());
            if (username != null) {
                System.out.println("token 口令验证通过！");
                ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("口令验证失败!");
                ctx.close();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage myMessage) throws Exception {
        System.out.println(TAG + "channelRead0:" + myMessage.getMsg_text());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(TAG + "userEventTriggered:");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        System.out.println(TAG + "acceptInboundMessage");
        return super.acceptInboundMessage(msg);
    }
}