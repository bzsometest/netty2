var PATH = "localhost:9901/ws";
var TOKEN = "chao123";
var message = {
	send_user: "system",
	receive_user: "",
	msg_text: ""
};

var websocket = null;

function openSocket() {
	if(window['WebSocket']) {
		// ws://host:port/project/websocketpath
		websocket = new WebSocket("ws://" + PATH + "?token=" + TOKEN);
	} else {
		console.log("new SockJS");
		websocket = new new SockJS(PATH + '/websocket/socketjs');
	}
	websocket.onopen = function(event) {
		console.log('open', event);
	}

	websocket.onmessage = function(event) {
		console.log("onmessage");
		console.log(event.data);
		try {
			var jsObj = JSON.parse(event.data);
			var msg2 = jsObj.send_user + ":" + jsObj.msg_text;
			$(' #message_div > ul').append('<li>' + msg2 + '</li>');
		} catch(e) {
			$(' #message_div > ul').append('<li>' + event.data + '</li>');
		}
	}
	websocket.onclose = function() {
		console.log("连接关闭");
	}
	websocket.error = function() {
		console.log("连接错误");
	}

}

//发送消息
function send() {
	message.receive_user = $("#username").val();
	message.msg_text = $("#msg_txt").val();
	console.log("send");
	console.log(message);
	websocket.send(JSON.stringify(message));
}

function set_token() {
	TOKEN = $("#token_text").val();
	openSocket();
}

function set_path() {
	path = $("#server_path").val();
}
$(function() {
	$("#token_text").val(TOKEN);
	$("#server_path").val(PATH);
})