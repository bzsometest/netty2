package com.chao.bean;

public class Test {
    public static void main(String[] args) {
        UserBean u=new UserBean();
        u.setUsername("chao");
        u.setEmail("123@qq.com");
        ResponseMessage responseMessage=  ResponseMessage.success().add("user",u);
        UserBean userBean = (UserBean) responseMessage.getExtend().get("user");
        System.out.println(userBean.getUsername());

    }
}
