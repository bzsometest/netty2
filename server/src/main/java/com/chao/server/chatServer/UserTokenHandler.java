package com.chao.server.chatServer;

import com.chao.domian.MyMessage;
import com.chao.domian.UserManager;
import com.chao.domian.UserToken;
import com.chao.server.channel.ChannelManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class UserTokenHandler extends SimpleChannelInboundHandler<UserToken> {

    private final static String TAG = "UserTokenHandler:";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UserToken userToken) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(TAG + "channelRead");
        if (msg instanceof UserToken) {
            String token = ((UserToken) msg).getToken();
            String username = UserManager.getUser(token);
            if (username != null) {
                System.out.println("用户成功登陆：" + username + ",Chat");

                MyMessage myMessage = new MyMessage();
                myMessage.setSend_user(username);
                myMessage.setMsg_text("登录成功！");

                ctx.channel().writeAndFlush(myMessage);
                ChannelManager.add(username, ctx.channel());
                UserManager.setUserNetType(username, UserManager.NET_TYPE_CHAT);

            } else {
                System.out.println("用户口令不正确！");
                MyMessage myMessage = new MyMessage();
                myMessage.setSend_user("system");
                myMessage.setMsg_text("口令不正确！！");
                ctx.channel().writeAndFlush(myMessage);
                ctx.close();
            }
        }
        super.channelRead(ctx, msg);
    }
}
