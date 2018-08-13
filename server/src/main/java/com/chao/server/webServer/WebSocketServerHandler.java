package com.chao.server.webServer;

import com.chao.domian.MessageManager;
import com.chao.domian.MyMessage;
import com.chao.server.channel.ChannelManager;
import com.chao.server.channel.ChannelMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端 channel
 *
 * @author waylau.com
 * @date 2015-2-16
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> { // (1)

    private final static Logger logger = LoggerFactory.getLogger(ChannelManager.class);
    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        logger.info("加入：{}", ctx.channel().remoteAddress());
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        logger.info("在线：{}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        MyMessage myMessage = MessageManager.getToMessage(textWebSocketFrame.text());

        logger.info("收到消息：{}", myMessage.getMsg_text());
        ChannelMessage.handlerMessage(ctx.channel(), myMessage);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        logger.info("掉线：{}", ctx.channel().remoteAddress());
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
        //  super.userEventTriggered(ctx, msg);
        logger.info("serEventTriggered");
        super.channelRead(ctx, msg);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("异常：{}", ctx.channel().remoteAddress());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead Object");
        super.channelRead(ctx, msg);
    }
}