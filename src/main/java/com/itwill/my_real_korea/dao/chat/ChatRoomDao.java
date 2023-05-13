package com.itwill.my_real_korea.dao.chat;

import java.util.List;

import com.itwill.my_real_korea.dto.chat.ChatRoom;

public interface ChatRoomDao {

	// 채팅방 목록 보기
	List<ChatRoom> selectAll(String userId);

	// 채팅방 목록 선택 기능 (roomNo로 채팅방 1개 보기)
	public ChatRoom selectByRoomName(String roomName);

	// 채팅방 목록 선택 기능 (roomName+receiveId로 채팅방 1개 보기)
	public List<ChatRoom> selectByRoomNameWith(String roomName);

	// from_id, to_id로 채팅방 찾기
	ChatRoom selectById(String fromId, String toId);

	// 채팅 생성 중복 체크
	List<ChatRoom> selectExist(String fromId, String toId);

	// 채팅방 생성
	int insertChatRoom(ChatRoom chatRoom);

	// 채팅방 삭제
	int deleteChatRoom(String roomName);

	// 1개의 채팅방 안 읽은 메세지 수
	int countNotReadInChatRoom(int roomNo, String userId);

}
