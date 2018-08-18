/*
用户登录相关
 */

var login_submit = "#login_btn";//登录按钮
var login_form = "#loginFrom";//登录表单

$(login_submit).click(function (event) {
    event.preventDefault();
    submit_disenable();
    ajaxLogin();
});

fromVar();

function fromVar() {
    $(login_form).validate({
        rules: {
            username: {
                required: true,
                minlength: 3
            },
            password: {
                required: true,
                minlength: 5
            }
        },
        messages: {
            username: {
                required: "请输入用户名",
                minlength: "用户名长度不能小于 3 位"
            },
            password: {
                required: "请输入密码",
                minlength: "密码长度不能小于 5 位"
            }
        }
    });
}

function ajaxLogin() {
    $.ajax({
        type: "post",
        url: _settings.api.login,
        data: $(login_form).serialize(),
        dataType: "json",
        success: function (result) {
            console.log(result);
            if (result.code == 200) { //登陆成功
                showLogin_success();
                window.location.href = _settings.html.userCenter;
            } else {
                showLogin_error();
            }
        },
        error: function () {
            showNet_error();
        },
        complete: function () {
            // Handle the complete event
            submit_enable();
        }
    });
}

var classes = "";//提交按钮新class
var oldClasses = "";//提交按钮原class
function submit_disenable() {
    oldClasses = $(login_submit).attr("class");
    classes = oldClasses + " disabled";
    $(login_submit).attr("class", classes);
    $(login_submit).html("<i class='icon-refresh icon-spin'></i> 正在登陆...");
}

function submit_enable() {
    $(login_submit).attr("class", oldClasses);
    $(login_submit).removeClass("disabled");
    $(login_submit).html("登录");
}

function showNet_error() {
    var tips = "登录失败，请检查网络！";
    alertBox(tips, "warning")
}

function showLogin_error() {
    var tips = "账号或密码错误，请重新输入！";
    alertBox(tips, "warning")
}

function showLogin_success() {
    var tips = "登录成功，欢迎回来！";
    alertBox(tips, "success")
}

//JS操作cookies方法!

//写cookies
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + value + ";expires=" + exp.toGMTString() + ";path=/";
}

function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1
            c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}

//删除cookies
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
}