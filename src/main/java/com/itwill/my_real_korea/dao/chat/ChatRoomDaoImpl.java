package com.itwill.my_real_korea.dao.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.mapper.ChatMsgMapper;
import com.itwill.my_real_korea.mapper.ChatRoomMapper;

@Repository
public class ChatRoomDaoImpl implements ChatRoomDao {

	@Autowired
	private ChatRoomMapper chatRoomMapper;

	public ChatRoomDaoImpl() {
		System.out.println("ChatRoomDaoImpl 기본생성자 호출");
	}

	public ChatRoomMapper getChatRoomMapper() {
		return chatRoomMapper;
	}

	public void setChatRoomMapper(ChatRoomMapper chatRoomMapper) {
		System.out.println(">>> ChatRoomDaoImpl():setChatRoomMapper()호출");
		this.chatRoomMapper = chatRoomMapper;
	}

	@Override
	public List<ChatRoom> selectAll(String userId) {
		return chatRoomMapper.selectAll(userId);
	}

	@Override
	public ChatRoom selectByRoomName(String roomName) {
		return chatRoomMapper.selectByRoomName(roomName);
	}

	@Override
	public ChatRoom selectById(String fromId, String toId) {
		Map<String, Object> idMap = new HashMap<>();
		idMap.put("fromId", fromId);
		idMap.put("toId", toId);
		return chatRoomMapper.selectById(idMap);
	}

	@Override
	public List<ChatRoom> selectExist(String fromId, String toId) {
		Map<String, Object> idMap = new HashMap<>();
		idMap.put("fromId", fromId);
		idMap.put("toId", toId);
		return chatRoomMapper.selectExist(idMap);
	}

	@Override
	public int insertChatRoom(ChatRoom chatRoom) {
		return chatRoomMapper.insertChatRoom(chatRoom);
	}

	@Override
	public int deleteChatRoom(String roomName) {
		return chatRoomMapper.deleteChatRoom(roomName);
	}

	@Override
	public int countNotReadInChatRoom(int roomNo, String userId) {
		Map<String, Object> chatMap = new HashMap<>();
		chatMap.put("roomNo", roomNo);
		chatMap.put("userId", userId);
		return chatRoomMapper.countNotReadInChatRoom(chatMap);
	}

	@Override
	public List<ChatRoom> selectByRoomNameWith(String roomName) {
		return chatRoomMapper.selectByRoomNameWith(roomName);
	}

}
