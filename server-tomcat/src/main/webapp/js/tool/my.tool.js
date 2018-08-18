var myModal_div = "#myModal-div";
var my_alertBox = "#my_alertBox";

$(function () {
    //存在模态框div,则异步加载模态框
    if ($(myModal_div).length > 0) {
        loadMyModal(myModal_div);
    }
});

//接收模态框加载完成事件消息
$(document).on("click", myModal_div, function () {
    //加载用户数据
    //判断是否存在加载用户信息的方法
    if (typeof getUserData == 'function') {
        //模态框加载完成，则开始加载用户数据
        getUserData();
    }
});

/* 显示提示框 */
function alertBox(tip, color) {
    $("#warningTip").remove();
    var box = $("<div></div>");
    box.attr("id", "warningTip");
    box.attr("class", "alert alert-" + color + " alert-dismissible");
    box.attr("role", "alert");
    box.html("<button type='button' class='close' data-dismiss='alert' aria-label='Close'><spanaria-hidden='true'>&times;</span></button>" + tip);
    $(my_alertBox).html(box);
}

//显示模态框
function showMyModal(title, content, fun) {
    var myModal = $("#myModal");
    myModal.find("#title").text(title);
    myModal.find("#content").text(content);
    myModal.on('hide.bs.modal', fun);
    myModal.modal('show');
}


//加载模态框到指定div中
function loadMyModal(modal_div) {
    dynamicLoadJs("js/modal/myModal.html.js", function () {
        console.log("加载模态框成功！");
        $(modal_div).html(myModalUnicode);

        //发送模态框加载完成事件消息
        $(modal_div).click();
    });
}

/**
 * 动态加载JS
 * @param {string} url 脚本地址
 * @param {function} callback  回调函数
 */
function dynamicLoadJs(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;
    if (typeof(callback) == 'function') {
        script.onload = script.onreadystatechange = function () {
            if (!this.readyState || this.readyState === "loaded" || this.readyState === "complete") {
                callback();
                script.onload = script.onreadystatechange = null;
            }
        };
    }
    head.appendChild(script);
}