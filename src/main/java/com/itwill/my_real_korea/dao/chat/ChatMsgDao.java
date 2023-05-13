package com.itwill.my_real_korea.dao.chat;

import java.util.List;

import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;

public interface ChatMsgDao {

	// 채팅방 1개의 전체 대화보기
	List<ChatMsg> selectChatByRoomName(String roomName);

	// 채팅 메세지 1개 보기
	ChatMsg selectByMsgNo(int msgNo);

	// 채팅방 1개의 읽지 않은 메세지 보기
	List<ChatMsg> selectNotReadMsg(int roomNo, String userId);

	// 채팅방 1개의 읽지 않은 메세지 수
	int countNotReadMsg(int roomNo, String userId);

	// 읽지 않은 메세지 전체 보기
	List<ChatMsg> selectAllNotReadMsg(String userId);

	// 읽지 않은 메세지 총 개수
	int countAllNotReadMsg(String userId);

	// 메세지 읽음으로 변경
	int updateReadMsg(int roomNo, String userId);

	// 채팅 메세지 1개 삭제
	int deleteChatMsg(int msgNo);

	// 채팅 메세지 삭제 시 메세지 대체
	int updateDeletedMsg(int msgNo);

	// 채팅 생성
	int insertChatMsg(ChatMsg chatMsg);

}
