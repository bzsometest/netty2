package com.chao.controller;

import com.chao.bean.ResponseMessage;
import com.chao.bean.UserBean;
import com.chao.security.JWTUtil;
import com.chao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 用户登录控制
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseMessage userLogin(String username, String password) {
        userService.login(username, password);
        String token = JWTUtil.sign(username, password);
        return ResponseMessage.success().add("token", token);
    }

    @ResponseBody
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseMessage userRegister(UserBean user) {
       boolean isReg = userService.addUser(user);
        String token = JWTUtil.sign(user.getUsername(),user.getPassword());
        return ResponseMessage.success().add("token", token);
    }

    /**
     * 退出
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public ResponseMessage logout() {
        try {
            return ResponseMessage.success().add("logout", "退出成功！");
        } catch (Exception e) {
            return ResponseMessage.fail().add("logout", "退出出现错误！");
        }
    }

}
