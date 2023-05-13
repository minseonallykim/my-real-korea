package com.itwill.my_real_korea.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.itwill.my_real_korea.dto.chat.ChatRoom;

@Mapper
public interface ChatRoomMapper {

	// 채팅방 목록 보기
	public List<ChatRoom> selectAll(String userId);

	// 채팅방 목록 선택 기능 (roomName로 채팅방 1개 보기)
	public ChatRoom selectByRoomName(String roomName);

	// 채팅방 목록 선택 기능 (roomName+receiveId로 채팅방 1개 보기)
	public List<ChatRoom> selectByRoomNameWith(String roomName);

	// from_id, to_id로 채팅방 찾기
	public ChatRoom selectById(Map<String, Object> idMap);

	// from_id, to_id로 채팅 생성 중복 체크
	public List<ChatRoom> selectExist(Map<String, Object> idMap);

	// 채팅방 생성
	public int insertChatRoom(ChatRoom chatRoom);

	// 채팅방 삭제
	public int deleteChatRoom(String roomName);

	// 1개의 채팅방 안 읽은 메세지 수(roomNo, userId)
	public int countNotReadInChatRoom(Map<String, Object> chatMap);
}
