package com.chao.server.webServer;

import com.chao.domian.UserManager;
import com.chao.server.channel.ChannelManager;
import com.chao.utils.RequestParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(TAG + "channelRead");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println(TAG + "channelRead0");
        ctx.fireChannelRead(request.retain());                  //2
        RequestParser requestParser = new RequestParser(request);
        String token = requestParser.getRequestParams().get("token");
        String username = UserManager.getUser(token);
        if (username != null) {
            System.out.println("用户成功登陆：" + username + ",WebSocket");

            ctx.channel().writeAndFlush(new TextWebSocketFrame("用户成功登陆：" + username));
            ChannelManager.add(username, ctx.channel());
            UserManager.setUserNetType(username, UserManager.NET_TYPE_WEBSOCKET);

        } else {
            System.out.println("用户口令不正确！");
            ctx.channel().writeAndFlush(new TextWebSocketFrame("用户口令不正确！连接关闭。"));
            ctx.close();
        }
    }


}
