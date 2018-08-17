package com.chao.service;

import com.chao.utils.Constant;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttpTool {
    public static void getUser(String token, Callback callback) {
        final Request.Builder builder = new Request.Builder().url(Constant.WEB_OAUTH+"/token/check");
        builder.addHeader("Authorization", token);  //将请求头以键值对形式添加，可添加多个请求头
        final Request request = builder.build();
        final OkHttpClient client = new OkHttpClient.Builder().build();

        client.newCall(request).enqueue(callback);
    }
}
