package com.chao.server.webServer;

import com.chao.domian.UserManager;
import com.chao.server.channel.ChannelManager;
import com.chao.utils.RequestParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理 Http 请求
 *
 * @author waylau.com
 * @date 2015-3-26
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { //1

    private final static Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead Object");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        logger.info("channelRead");
        ctx.fireChannelRead(request.retain());                  //2
        RequestParser requestParser = new RequestParser(request);
        String token = requestParser.getRequestParams().get("token");
        String username = UserManager.getUser(token);
        if (username != null) {
            logger.info("用户成功登陆：{},来自{}", username, "WebSocket");

            ctx.channel().writeAndFlush(new TextWebSocketFrame("用户成功登陆：" + username));
            ChannelManager.add(username, ctx.channel(), ChannelManager.NET_TYPE_WEB_SOCKET);


        } else {
            logger.info("用户口令不正确，来自{}", username, "WebSocket");
            ctx.channel().writeAndFlush(new TextWebSocketFrame("用户口令不正确！连接关闭。"));
            ctx.close();
        }
    }


}
