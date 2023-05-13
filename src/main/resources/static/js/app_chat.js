import * as View from "./view.js";
import * as Request from "./request.js";

let sock;
let myId = $('#myId').val();
let receiverId = $('#receiverId').val();
let roomName = document.getElementById('roomName').textContent;
console.log(myId);
console.log(roomName);

let receiverIdFromRoom;
let senderIdFromRoom;
let clickChatRoomName;

const serializedList = $('#jsonMyChatRoomNameList').val();
const myChatRoomNameList = JSON.parse(serializedList);
console.log(myChatRoomNameList);


/*****************소켓 연결/해제*******************/
function onClose(evt) {
	console.log("소켓 연결 해제 : " + evt);
}
function onOpen(evt) {
	document.getElementById('chat-content').innerHTML = "";
	document.querySelector('#roomName').textContent = "";
	console.log("소켓 연결 성공 : " + evt);
}

/**********페이지 로딩 시 - 소켓연결, DB내용 가져오기*********/
$(document).ready(function() {
	sock = new WebSocket("ws://localhost:80/final-project-team2-my-real-korea/ws/chat");
	sock.onmessage = onMessage;
	sock.onopen = onOpen;
	sock.onClose = onClose;

	scrollDown();
	// DB에 저장된 대화내용 가져오기
	let receiverId = document.getElementById('receiverId').value;
	getChatFromDB(myId, receiverId);
});

/**********채팅메세지 보내기(버튼 클릭or엔터)*********/
function sendMessage() {
	let message = $("#msg").val();
	let msg = {
		"receiverId": receiverId,
		"message": message
	}
	sock.send(JSON.stringify(msg));
	$('#msg').val("");
}

$("#msg").keyup(e => {
	if (e.keyCode == 13) {
		sendMessage();
	}
});

$(document).on('click', '#button-send', function(e) {
	sendMessage();

});

/***************채팅방 나가기*******************/
$(document).on('click', '#disconn', function(e) {
	onClose();
	sock.onClose;
	console.log('채팅방 나가기');
	window.close();
});
/************ onMessage *************/
function onMessage(msg) {
	let data = JSON.parse(msg.data);
	let onlineList = data.onlineList;
	let senderId = data.senderId;
	let message = data.message;
	let time = data.time;
	let newOne = data.newOne;
	let outOne = data.outOne;
	console.log('msg.data >>> ', msg.data);
	console.log('onlineList >>>> ', onlineList);
	console.log('senderId >>> ', senderId)
	console.log('receiverId >>> ', senderIdFromRoom)
	console.log('message >>> ', message)
	console.log('time >>> ', time)
	console.log('newOne >>> ', newOne);
	console.log('outOne >>> ', outOne);

	// 메세지 보내기
	insertMessage(senderId, senderIdFromRoom, time, message);
	// DB에 저장
	saveChatDB(senderId, senderIdFromRoom, message, time);
	// scroll down
	scrollDown();
}

/************ 메세지 보내기 *************/
function insertMessage(senderId, receiverId, time, message) {
	console.log('insertMessage[myId] : ' + myId);
	console.log('insertMessage[senderId] : ' + senderId);
	console.log('insertMessage[receiverId] : ' + receiverId);
	let chatContent = document.querySelector("#chat-content");
	if (senderId == myId) {
		// 보내는 사람이 나 일 때(오른쪽 메세지)
		let li = document.createElement('li');
		li.classList.add('message-li', 'clearfix', 'float-right');
		let infoDiv = document.createElement('div');
		infoDiv.classList.add('message-data');
		li.appendChild(infoDiv);
		let timeSpan = document.createElement('span');
		timeSpan.classList.add('message-data-time');
		timeSpan.textContent = time;
		infoDiv.appendChild(timeSpan);
		let msgDiv = document.createElement('div');
		msgDiv.classList.add('message', 'my-message');
		msgDiv.textContent = message;
		li.appendChild(msgDiv);

		chatContent.appendChild(li);
	} else {
		// 보내는 사람 받는 사람 다를 때(왼쪽 메세지)
		let li = document.createElement('li');
		li.classList.add('message-li', 'clearfix');
		let infoDiv = document.createElement('div');
		infoDiv.classList.add('message-data');
		li.appendChild(infoDiv);
		let timeSpan = document.createElement('span');
		timeSpan.classList.add('message-data-time');
		timeSpan.textContent = time;
		infoDiv.appendChild(timeSpan);
		let msgDiv = document.createElement('div');
		msgDiv.classList.add('message', 'other-message');
		msgDiv.textContent = message;
		li.appendChild(msgDiv);

		chatContent.appendChild(li);
	}

}

/************ DB에서 메세지 가져오기 *************/
function getMessage(senderId, receiverId, userId, time, message) {
	console.log('getMessage[myId] : ' + myId);
	console.log('getMessage[senderId] : ' + senderId);
	console.log('getMessage[userId] : ' + userId);
	let chatContent = document.querySelector("#chat-content");
	if (senderId == userId) {
		// 보낸사람과 현재 로그인한 유저 같을 때(오른쪽 메세지)
		let li = document.createElement('li');
		li.classList.add('message-li', 'clearfix', 'float-right');
		let infoDiv = document.createElement('div');
		infoDiv.classList.add('message-data');
		li.appendChild(infoDiv);
		let timeSpan = document.createElement('span');
		timeSpan.classList.add('message-data-time');
		timeSpan.textContent = time;
		infoDiv.appendChild(timeSpan);
		let msgDiv = document.createElement('div');
		msgDiv.classList.add('message', 'my-message');
		msgDiv.textContent = message;
		li.appendChild(msgDiv);

		chatContent.appendChild(li);
	} else {
		// 보내는 사람 받는 사람 다를 때(왼쪽 메세지)
		let li = document.createElement('li');
		li.classList.add('message-li', 'clearfix');
		let infoDiv = document.createElement('div');
		infoDiv.classList.add('message-data');
		li.appendChild(infoDiv);
		let timeSpan = document.createElement('span');
		timeSpan.classList.add('message-data-time');
		timeSpan.textContent = time;
		infoDiv.appendChild(timeSpan);
		let msgDiv = document.createElement('div');
		msgDiv.classList.add('message', 'other-message');
		msgDiv.textContent = message;
		li.appendChild(msgDiv);

		chatContent.appendChild(li);
	}
}

/************ 채팅 내용 DB에 저장 *************/
function saveChatDB(senderId, receiverId, message, time) {
	let jsonData = {};
	document.querySelectorAll('.message-li').forEach(item => {
		let type = item.querySelector('.message').classList[1];
		let time = item.querySelector('.message-data > .message-data-time').textContent;
		let message = item.querySelector('.message').textContent;
		jsonData = {
			"myId": myId,
			"senderId": senderId,
			"receiverId": receiverId,
			"message": message,
			"roomName": clickChatRoomName,
			"time": time
		}
	})
	let url = 'save-chat';
	let method = 'POST';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = JSON.stringify(jsonData); // 메세지 전송데이터
	console.log('[savechat]sendData' + sendData);
	let async = true;

	Request.ajaxRequest(url, method, contentType,
		sendData,
		function(resultJson) {
			console.log('>>>>>>>>>>saveChatDB 접근');
			if (resultJson.code == 1) {
				console.log('saveChatDB 성공');
			} else {
				console.log(resultJson.msg);
			};
		}, async);
};

/************ 채팅 내용 가져오기(DB연결) *************/
function getChatFromDB(roomName, myId, receiverId) {
	let url = 'chat-detail-rest';
	let method = 'POST';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {
		"roomName": roomName,
		"senderId": myId,
		"receiverId": receiverId
	};
	let async = true;
	console.log(">>>>>>>>>getChatFromDB sendData" + JSON.stringify(sendData));
	Request.ajaxRequest(url, method, contentType,
		JSON.stringify(sendData),
		function(resultJson) {
			console.log(">>>>>>>>>getChatFromDB 접근");
			if (resultJson.code == 1) {
				let roomName = resultJson.roomName;
				let senderId = resultJson.senderId;
				let receiverId = resultJson.receiverId;
				let chatList = resultJson.data;

				for (const item of chatList) {
					getMessage(senderId, receiverId, item.userId, item.msgSendTime, item.msgContent);
				}
				console.log("getChatFromDB senderId :" + senderId);
			} else {
				console.log(resultJson.msg);
			};
		}, async);

};

/************ 스크롤 하단 고정 *************/
function scrollDown() {
	var chatContent = document.querySelector("#chat-content");
	chatContent.scrollTop = chatContent.scrollHeight;
};

/************ 채팅방 목록의 채팅방 클릭 ************/
$(document).on('click', '#chat-room-list', function(e) {
	// DB에 저장된 대화내용 가져오기
	clickChatRoomName = $(this).find('#master').text();
	console.log('채팅방클릭-clickChatRoomName:' + clickChatRoomName);
	receiverIdFromRoom = clickChatRoomName.substr(clickChatRoomName.indexOf("&") + 1);
	senderIdFromRoom = clickChatRoomName.substring(0, clickChatRoomName.indexOf('&'));
	console.log('채팅방클릭-receiverIdFromRoom:' + receiverIdFromRoom);
	//window.location.href='chat?receiverId='+receiverIdFromRoom;
	scrollDown();
	document.getElementById('chat-content').innerHTML = "";
	getChatFromDB(clickChatRoomName, myId, receiverIdFromRoom);
	changeRoomName(clickChatRoomName);
});

/************ 채팅방 목록 클릭 시 해당 채팅방 이름 변경 ************/
function changeRoomName(chatRoomName) {
	let roomNamediv = document.querySelector('#roomName');
	roomNamediv.textContent = chatRoomName;
};


/**************채팅 아이디 가져오기****************/

function getUserId() {
	let url = 'get-chat-id';
	let method = 'GET';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;
	let userId = null;
	Request.ajaxRequest(url, method, contentType,
		sendData,
		function(resultJson) {
			if (resultJson.code == 1) {
				userId = resultJson.userId;
				console.log("채팅아이디 :" + userId);
			} else {
				alert(resultJson.msg);
			};
		}, async);
	return userId;
}

/**************안읽은 채팅 수 가져오기***************/
function getChatNum(userId) {
	let roomNo = '1';
	let url = `count-not-read-chat?userId=${userId}&roomNo=${roomNo}`;
	let method = 'GET';
	let contentType = 'application/json;charset=UTF-8';
	let sendData = {};
	let async = true;
	Request.ajaxRequest(url, method, contentType,
		sendData,
		function(resultJson) {
			//code 1 , 안읽은 채팅이 있을 때
			if (resultJson.code == 1 && resultJson.data != 0) {
				console.log('안읽은채팅 있음');
				// 안읽은 채팅이 0 일 때	
			} else if (resultJson.code == 1 && resultJson.data == 0) {
				console.log('안읽은채팅 없음');
			} else {
				alert(resultJson.msg);
			};
		}, async);
}

