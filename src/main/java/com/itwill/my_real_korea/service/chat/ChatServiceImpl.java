package com.itwill.my_real_korea.service.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.my_real_korea.dao.chat.ChatMsgDao;
import com.itwill.my_real_korea.dao.chat.ChatRoomDao;
import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;

@Service
public class ChatServiceImpl implements ChatService {

	private ObjectMapper objectMapper;

	@Autowired
	private ChatRoomDao chatRoomDao;

	@Autowired
	private ChatMsgDao chatMsgDao;

	public ChatServiceImpl() {
	}

	/************* ChatRoom **************/

	// 채팅방 목록 보기
	@Override
	public List<ChatRoom> selectAll(String userId) {
		return chatRoomDao.selectAll(userId);
	}

	// 채팅방 목록 선택 기능 (roomNo로 채팅방 1개 보기)
	@Override
	public ChatRoom selectByRoomName(String roomName) {
		return chatRoomDao.selectByRoomName(roomName);
	}

	/*
	 * 채팅방 중복 체크
	 */
	@Override
	public boolean duplicateCheck(String fromId, String toId) {
		List<ChatRoom> chatRoomExistList = chatRoomDao.selectExist(fromId, toId);
		if (chatRoomExistList.size() >= 1) {
			// fromId, toId 의 채팅방 중복
			return true;
		} else {
			return false;
		}
	}

	// 채팅방 생성
	@Override
	public int insertChatRoom(ChatRoom chatRoom) {
		return chatRoomDao.insertChatRoom(chatRoom);
	}

	// 채팅방 삭제
	@Override
	public int deleteChatRoom(String roomName) {
		return chatRoomDao.deleteChatRoom(roomName);
	}

	// 1개의 채팅방 안 읽은 메세지 수
	@Override
	public int countNotReadInChatRoom(int roomNo, String userId) {
		return chatRoomDao.countNotReadInChatRoom(roomNo, userId);
	}

	// 지정한 웹소켓(세션)에 메세지 전송
	public void sendChatMsg(WebSocketSession session, ChatMsg chatMsg) {
		try {
			// chatMsg 객체를 JSON 형태로 변경해서 세션에 전송
			session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(chatMsg)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************* ChatMsg **************/

	// 채팅방 1개의 전체 대화보기
	@Override
	public List<ChatMsg> selectChatByRoomName(String roomName) {
		return chatMsgDao.selectChatByRoomName(roomName);
	}

	// 채팅 메세지 1개 보기
	@Override
	public ChatMsg selectByMsgNo(int msgNo) {
		return chatMsgDao.selectByMsgNo(msgNo);
	}

	// 채팅방 1개의 읽지 않은 메세지 보기
	@Override
	public List<ChatMsg> selectNotReadMsg(int roomNo, String userId) {
		chatMsgDao.updateReadMsg(roomNo, userId); // 읽음으로 변경
		return chatMsgDao.selectNotReadMsg(roomNo, userId);
	}

	// 채팅방 1개의 읽지 않은 메세지 수
	@Override
	public int countNotReadMsg(int roomNo, String userId) {
		return chatMsgDao.countNotReadMsg(roomNo, userId);
	}

	// 읽지 않은 메세지 전체 보기
	@Override
	public List<ChatMsg> selectAllNotReadMsg(String userId) {
		return chatMsgDao.selectAllNotReadMsg(userId);
	}

	// 읽지 않은 메세지 총 개수
	@Override
	public int countAllNotReadMsg(String userId) {
		return chatMsgDao.countAllNotReadMsg(userId);
	}

	// 메세지 읽음으로 변경
	@Override
	public int updateReadMsg(int roomNo, String userId) {
		return chatMsgDao.updateReadMsg(roomNo, userId);
	}

	// 채팅 메세지 1개 삭제
	@Override
	public int deleteChatMsg(int msgNo) {
		return chatMsgDao.deleteChatMsg(msgNo);
	}

	// 채팅 메세지 삭제 시 메세지 대체
	@Override
	public int updateDeletedMsg(int msgNo) {
		return chatMsgDao.updateDeletedMsg(msgNo);
	}

	// 채팅 메세지 생성
	@Override
	public int insertChatMsg(ChatMsg chatMsg) {
		return chatMsgDao.insertChatMsg(chatMsg);
	}

	@Override
	public List<ChatRoom> selectByRoomNameWith(String roomName) {
		return chatRoomDao.selectByRoomNameWith(roomName);
	}

}
