package com.chao.server.chatclient;

import com.chao.domain.MyMessage;
import com.chao.domain.UserManager;
import com.chao.domain.UserToken;
import com.chao.server.channel.ChannelManager;
import com.chao.service.MyBack;
import com.chao.service.UserService;
import com.chao.utils.RequestParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTokenHandler extends SimpleChannelInboundHandler<UserToken> {

    private final static Logger logger = LoggerFactory.getLogger(UserTokenHandler.class);
    UserService userService = new UserService();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, UserToken userToken) throws Exception {
        logger.info("channelRead0");
        // ctx.fireChannelRead(userToken);                  //2

        //   ctx.channel().writeAndFlush(new TextWebSocketFrame("正在登录，请稍后..."));


        final String token = userToken.getToken();

        final ChannelHandlerContext ctx2 = ctx;
        userService.getUser(token, new MyBack() {
            @Override
            public void error() {
                logger.info("用户口令不正确，来自{}", "ChatClient");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("用户口令不正确！连接关闭。"));
                ctx.close();
            }

            @Override
            public void successs() {
                String username = UserManager.getUser(token);
                logger.info("用户成功登陆：{},来自{}", username, "ChatClient");
                ctx2.channel().writeAndFlush(new MyMessage("system", "", "认证成功！"));
                ChannelManager.add(username, ctx.channel(), ChannelManager.NET_TYPE_CHAT_CLIENT);
            }
        });
    }
}
