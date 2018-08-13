package com.chao.server.chatServer;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.chao.domian.UserToken;
import com.chao.server.channel.ChannelManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTokenHandler extends SimpleChannelInboundHandler<UserToken> {

    private final static Logger logger = LoggerFactory.getLogger(UserTokenHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserToken userToken) throws Exception {
        String token = userToken.getToken();
        String username = UserManager.getUser(token);
        if (username != null) {
            logger.info("用户成功登陆：{},来自{}", username, "ChatClient");

            MyMessage myMessage = new MyMessage();
            myMessage.setSend_user(username);
            myMessage.setMsg_text("登录成功！");

            ctx.channel().writeAndFlush(myMessage);
            ChannelManager.add(username, ctx.channel());
            UserManager.setUserNetType(username, UserManager.NET_TYPE_CHAT);

        } else {
            logger.info("用户口令不正确，来自{}", username, "ChatClient");

            MyMessage myMessage = new MyMessage();
            myMessage.setSend_user("system");
            myMessage.setMsg_text("口令不正确！！");
            ctx.channel().writeAndFlush(myMessage);
            ctx.close();
        }
    }
}
