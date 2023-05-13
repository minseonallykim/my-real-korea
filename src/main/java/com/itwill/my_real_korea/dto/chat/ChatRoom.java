package com.itwill.my_real_korea.dto.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import com.itwill.my_real_korea.service.chat.ChatService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/*
 이름        널?       유형            
--------- -------- ------------- 
ROOM_NO   NOT NULL NUMBER        
ROOM_NAME NOT NULL VARCHAR2(100) 
FROM_ID            VARCHAR2(50)  
TO_ID              VARCHAR2(50)  

 */

@Data
@NoArgsConstructor
@ToString
public class ChatRoom {

	// private int roomNo;
	private String roomName;
	// list<ChatMsg> - ChatMsg 의 정보를 가져오기 위함 (1:N)
	private List<ChatMsg> chatMsgList;

	// 해당 채팅방에 입장한 채팅 유저들의 세션 정보 리스트
	private Set<WebSocketSession> sessions = new HashSet<>();

	public ChatRoom(String roomName) {
		super();
		this.roomName = roomName;
		this.chatMsgList = new ArrayList<>();
	}

	public void handleAction(WebSocketSession session, ChatMsg chatMsg, ChatService chatService) {
		if (chatMsg.getMsgContent() != null) {
			// 채팅방에 세션 추가
			sessions.add(session);
			chatMsg.setMsgContent(chatMsg.getUserId() + "님이 입장하셨습니다.");
			// 다른 채팅유저들에게 알리기
			sendChatmsg(chatMsg, chatService);
		}
	}

	/*
	 * 현재 채팅방에 접속한 모든 유저들의 WebSocketSession 을 sessions에서 가져와 채팅 메세지를 DB에 저장 => 채팅방에
	 * 접속한 모든 유저들에게 새로운 채팅 내용 실시간 전달
	 * 
	 * parallelStream() : 병렬처리 -> 대량 데이터 처리할 때 속도 빠름
	 */
	public void sendChatmsg(ChatMsg chatMsg, ChatService chatService) {
		sessions.parallelStream().forEach(session -> chatService.insertChatMsg(chatMsg));
	}

}
