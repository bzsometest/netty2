package com.chao.server;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;

public class MyHttp extends DefaultFullHttpRequest {

    public MyHttp(HttpVersion httpVersion, HttpMethod method, String uri) {
        super(httpVersion, method, uri);
    }
}
