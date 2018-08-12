package com.chao.server;

import com.chao.utils.RequestParser;
import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 处理 Http 请求
 *
 * @author waylau.com
 * @date 2015-3-26
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { //1


    private final static String TAG = "HttpRequestHandler:";

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println(TAG + "channelRead0");
        ctx.fireChannelRead(request.retain());                  //2

        RequestParser requestParser = new RequestParser(request);
        String token = requestParser.getRequestParams().get("token");
        String user = UserManager.getUser(token);
        if (user == null) {
            System.out.println("token错误");
            //  ctx.channel().writeAndFlush(new TextWebSocketFrame("token错误，登录失败。"));
            // ctx.close();
        } else {
            System.out.println("连接成功：" + user);

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(TAG + "channelRead");
        if (msg instanceof MyMessage) {
            System.out.println("MyMessage");
        }
        if (msg instanceof TextWebSocketFrame) {
            System.out.println("TextWebSocketFrame");
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        System.out.println(TAG + "acceptInboundMessage");
        if (msg instanceof MyMessage) {
            System.out.println("MyMessage");
        }
        if (msg instanceof TextWebSocketFrame) {
            System.out.println("TextWebSocketFrame");
        }
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(TAG + "userEventTriggered");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(TAG + "Client:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
