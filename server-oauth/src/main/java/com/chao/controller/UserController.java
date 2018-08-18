package com.chao.controller;

import com.chao.bean.ResponseMessage;
import com.chao.bean.UserBean;
import com.chao.security.JWTUtil;
import com.chao.service.UserService;
import com.chao.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 用户登录控制
 */
@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseMessage getUser(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(Constants.AUTHORIZATION);
        String username = JWTUtil.getUsername(token);
        UserBean user = userService.getUserByUsername(username);
        return ResponseMessage.success().add("user", user);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseMessage userLogin(String username, String password) {
        UserBean userBean = userService.login(username, password);
        if (userBean == null) {
            return ResponseMessage.fail();
        }
        String token = JWTUtil.sign(username, password);
        return ResponseMessage.success().add("token", token).add("user", userBean);
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseMessage userRegister(UserBean user) {
        boolean isReg = userService.addUser(user);
        String token = JWTUtil.sign(user.getUsername(), user.getPassword());
        return ResponseMessage.success().add("token", token);
    }

    /**
     * 退出
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseMessage logout() {
        try {
            return ResponseMessage.success().add("logout", "退出成功！");
        } catch (Exception e) {
            return ResponseMessage.fail().add("logout", "退出出现错误！");
        }
    }
}
