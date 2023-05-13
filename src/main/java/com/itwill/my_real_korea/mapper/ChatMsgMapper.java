package com.itwill.my_real_korea.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.itwill.my_real_korea.dto.chat.ChatMsg;

@Mapper
public interface ChatMsgMapper {

	// 채팅방 1개의 전체 대화보기
	public List<ChatMsg> selectChatByRoomName(String roomName);

	// 채팅 메세지 1개 보기
	public ChatMsg selectByMsgNo(int msgNo);

	// 채팅방 1개의 읽지 않은 메세지 보기 (roomNo, userId)
	public List<ChatMsg> selectNotReadMsg(Map<String, Object> msgMap);

	// 채팅방 1개의 읽지 않은 메세지 수 (roomNo, userId)
	public int countNotReadMsg(Map<String, Object> msgMap);

	// 읽지 않은 메세지 전체 보기
	public List<ChatMsg> selectAllNotReadMsg(String userId);

	// 읽지 않은 메세지 총 개수
	public int countAllNotReadMsg(String userId);

	// 메세지 읽음으로 변경(roomNo, userId)
	public int updateReadMsg(Map<String, Object> msgMap);

	// 채팅 메세지 1개 삭제
	public int deleteChatMsg(int msgNo);

	// 채팅 메세지 삭제 시 메세지 대체
	public int updateDeletedMsg(int msgNo);

	// 채팅 생성
	public int insertChatMsg(ChatMsg chatMsg);
}
