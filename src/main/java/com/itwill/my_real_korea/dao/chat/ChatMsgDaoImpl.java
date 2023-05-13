package com.itwill.my_real_korea.dao.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.mapper.ChatMsgMapper;
import com.itwill.my_real_korea.mapper.NoticeMapper;

@Repository
public class ChatMsgDaoImpl implements ChatMsgDao {

	@Autowired
	private ChatMsgMapper chatMsgMapper;

	public ChatMsgDaoImpl() {
		System.out.println("ChatMsgDaoImpl 기본생성자 호출");
	}

	public ChatMsgMapper getChatMsgMapper() {
		return chatMsgMapper;
	}

	public void setChatMsgMapper(ChatMsgMapper chatMsgMapper) {
		System.out.println(">>> ChatMsgDaoImpl():setChatMsgMapper()호출");
		this.chatMsgMapper = chatMsgMapper;
	}

	@Override
	public List<ChatMsg> selectChatByRoomName(String roomName) {
		return chatMsgMapper.selectChatByRoomName(roomName);
	}

	@Override
	public ChatMsg selectByMsgNo(int msgNo) {
		return chatMsgMapper.selectByMsgNo(msgNo);
	}

	@Override
	public List<ChatMsg> selectNotReadMsg(int roomNo, String userId) {
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("roomNo", roomNo);
		msgMap.put("userId", userId);
		return chatMsgMapper.selectNotReadMsg(msgMap);
	}

	@Override
	public int countNotReadMsg(int roomNo, String userId) {
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("roomNo", roomNo);
		msgMap.put("userId", userId);
		return chatMsgMapper.countNotReadMsg(msgMap);
	}

	@Override
	public List<ChatMsg> selectAllNotReadMsg(String userId) {
		return chatMsgMapper.selectAllNotReadMsg(userId);
	}

	@Override
	public int countAllNotReadMsg(String userId) {
		return chatMsgMapper.countAllNotReadMsg(userId);
	}

	@Override
	public int updateReadMsg(int roomNo, String userId) {
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("roomNo", roomNo);
		msgMap.put("userId", userId);
		return chatMsgMapper.updateReadMsg(msgMap);
	}

	@Override
	public int deleteChatMsg(int msgNo) {
		return chatMsgMapper.deleteChatMsg(msgNo);
	}

	@Override
	public int updateDeletedMsg(int msgNo) {
		return chatMsgMapper.updateDeletedMsg(msgNo);
	}

	@Override
	public int insertChatMsg(ChatMsg chatMsg) {
		return chatMsgMapper.insertChatMsg(chatMsg);
	}

}
