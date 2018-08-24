/**
 * 全局配置文件
 * @type {string}
 */

//服务器api接口地址
var api_path = "http://localhost:8080/server";
//var websocket = "http://gobang03.bzchao.com/server";
_settings = {
    gobangDown: "http://t.cn/RrXswvP",
    jdkDown: "http://down-www.newasp.net/pcdown/soft/yh/jre1.8x64.7z",
    api: {
        user: api_path + "/user/user",//用户操作接口(CRUD)
        login: api_path + "/user/login",//用户登录接口地址
        logout: api_path + "/user/logout",//注销登录接口地址
        register: api_path + "/user/register",//用户注册接口(CRUD)
        check: api_path + "/check",//信息后端检验
    },
    html: {
        index: "index.html",
        login: "login.html",
        userCenter: "userCenter.html",
        download: "download.html"
    }
}
