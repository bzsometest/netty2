package com.chao;

import com.chao.domian.MyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端 channel
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<MyMessage> {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("SimpleChatClient:在线");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage myMessage) throws Exception {
        System.out.println(myMessage.getMsg_text());
    }
}
