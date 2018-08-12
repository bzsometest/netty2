package com.chao.server.webServer;

import com.chao.domian.MessageManager;
import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.chao.domian.UserToken;
import com.chao.server.channel.ChannelManager;
import com.chao.server.channel.ChannelMessage;
import com.chao.utils.RequestParser;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
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
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        super.channelActive(ctx);
        Channel incoming = ctx.channel();
        ctx.channel().writeAndFlush(new TextWebSocketFrame("[SERVERc] - " + incoming.remoteAddress() + " 在线"));
        System.out.println(TAG + "channelActive:" + incoming.remoteAddress() + "在线");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println(TAG + "channelRead0:消息" + textWebSocketFrame.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("[me] : " + textWebSocketFrame.text()));

        MyMessage myMessage = MessageManager.getToMessage(textWebSocketFrame.text());
        ChannelMessage.handlerMessage(ctx.channel(), myMessage);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println(TAG + "channelInactive" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        System.out.println(TAG + "acceptInboundMessage");
        if (msg instanceof HttpRequest) {
            System.out.println("FullHttpRequest");
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestParser requestParser = new RequestParser(request);
            String token = requestParser.getRequestParams().get("token");
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username);
              //  ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("用户口令不正确！");
            }
        }
        if (msg instanceof WebSocketFrame) {
            System.out.println("WebSocketFrame)");
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestParser requestParser = new RequestParser(request);
            String token = requestParser.getRequestParams().get("token");
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username);
               // ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("用户口令不正确！");
            }
        }

        return super.acceptInboundMessage(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
      //  super.userEventTriggered(ctx, msg);
        System.out.println(TAG + "serEventTriggered");
        if (msg instanceof FullHttpRequest) {
            System.out.println("FullHttpRequest");
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestParser requestParser = new RequestParser(request);
            String token = requestParser.getRequestParams().get("token");
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username);
                ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("用户口令不正确！");
            }
        }
        if (msg instanceof WebSocketFrame) {
            System.out.println("WebSocketFrame)");
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestParser requestParser = new RequestParser(request);
            String token = requestParser.getRequestParams().get("token");
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username);
                ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("用户口令不正确！");
            }
        }
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(TAG + "channelRead Object");
        super.channelRead(ctx, msg);
        if (msg instanceof FullHttpRequest) {
            System.out.println("FullHttpRequest");
            FullHttpRequest request = (FullHttpRequest) msg;
            RequestParser requestParser = new RequestParser(request);
            String token = requestParser.getRequestParams().get("token");
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username);
                ChannelManager.add(username, ctx.channel());
            } else {
                System.out.println("用户口令不正确！");
            }
        }
    }
}