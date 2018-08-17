package com.chao.controller;

import com.chao.bean.ResponseMessage;
import com.chao.bean.UserBean;
import com.chao.security.JWTUtil;
import com.chao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/token")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TokenController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/check")
    public ResponseMessage checkUser(@RequestHeader HttpHeaders headers) {
        String token=headers.getFirst("Authorization");
        String username=JWTUtil.getUsername(token);
        UserBean user = userService.getUserByUsername(username);
        return ResponseMessage.success().add("user", user);
    }

}