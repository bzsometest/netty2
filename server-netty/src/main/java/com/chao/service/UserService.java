package com.chao.service;

import com.chao.domain.ResponseMessage;
import com.chao.domain.UserBean;
import com.chao.domain.UserManager;
import com.chao.server.channel.ChannelManager;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);


    public void getUser(final String token, final MyBack back) {
        OkHttpTool.getUser(token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (back != null) {
                    back.error();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                logger.info("netty服务器消息：{}", str);
                ResponseMessage responseMessage = new Gson().fromJson(str, ResponseMessage.class);
                if (responseMessage != null && responseMessage.getCode() == 200) {
                    Map<String, Object> map = responseMessage.getExtend();
                    Object object = map.get("user");
                    //  UserBean user=(UserBean)object;//强制转换会出错！
                    UserBean user = new Gson().fromJson(new Gson().toJson(object), UserBean.class);
                    UserManager.add(user.getUsername(), token);
                    if (back != null) {
                        back.successs();
                        return;
                    }
                }
                back.error();
            }
        });
    }
}
