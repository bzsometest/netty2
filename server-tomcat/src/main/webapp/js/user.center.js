var user_div = "#user-table"

//接收用户信息加载完成事件消息
$(document).on("click", "#userdata_Tip", function () {
    show_user_bar();
    show_user_center();
});

function show_user_center() {
    if (!isLoginAndShow()) {
        return;
    }
    $(user_div).find("#username").text(userData.username);
    $(user_div).find("#email").text(userData.email);
    $(user_div).find("#sign").text(userData.sign);
    //使用时间格式化插件格式化时间
    var time = moment(userData.regTime).format('YYYY-MM-DD HH:mm:ss');
    $(user_div).find("#reg_time").text(time);
}