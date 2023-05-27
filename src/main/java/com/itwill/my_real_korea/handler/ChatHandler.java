package com.itwill.my_real_korea.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jasper.tagplugins.jstl.core.If;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.chat.ChatService;

import lombok.extern.log4j.Log4j2;
import springfox.documentation.spring.web.json.Json;

/*
 * 클라이언트와 서버 간의 실시간 소통 담당
 */
@Component
@Log4j2 // log 변수를 사용하여 로그 기록 가능
public class ChatHandler extends TextWebSocketHandler {
	// 접속자 아이디 리스트
	private static List<String> onlineList = new ArrayList<>();
	// 세션 리스트
	private static List<WebSocketSession> sessionList = new ArrayList<>();
	// 전체 채팅방에 접속한 모든 사용자들의 세션 정보
	public static Map<String, WebSocketSession> userSession = new HashMap<>();
	// ObjectMapper : JSON 데이터를 java객체로, java객체를 JSON데이터로 변환
	@Autowired
	ObjectMapper json = new ObjectMapper();
	// 클라이언트 접속 성공 : 세션 생성, 관리
	
	// websocket에 session이 접속했을 때, 처리하는 메소드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		// 보내는 사람 아이디(세션아이디) : 세션 리스트에 저장
		User loginUser = (User) session.getAttributes().get("loginUser");
		String senderId = loginUser.getUserId();
		sessionList.add(session);
		onlineList.add(senderId);
		userSession.put(senderId, session);

		log.info("보내는 사람 : sessionId >>> " + senderId);

		Map<String, Object> data = new HashMap<>();
		data.put("senderId", senderId);
		data.put("message", senderId + " 님이 접속했습니다.");
		data.put("newOne", senderId);

		log.info("afterConnectionEstablished final data >>> " + data);

		try {
			// 세션에 있는 유저에게 접속 성공 메세지 돌림
			if (userSession.size() > 0) {
				TextMessage msg = new TextMessage(senderId + " 님이 접속했습니다.");
				sendToAll(msg, senderId);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(session + " 세션접속 성공");
	}

	// websocket을 통해서 받은 메세지를 처리하는 메소드
	// 클라이언트로부터 메세지 수신 : 로그 출력, 메세지 클라이언트에게 전송 (payload : 전송되는 데이터)
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		Map<String, Object> dataMap = new HashMap<>();
		// 보내는 시간
		LocalDateTime currentTime = LocalDateTime.now();
		String time = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a E"));
		// 메세지 데이터
		String payload = message.getPayload();
		// 보내는 사람
		User loginUser = (User) session.getAttributes().get("loginUser");
		String senderId = loginUser.getUserId();
		// 받는 사람
		String receiverId = "";
		if (onlineList.size() >= 2) {
			String tempId1 = onlineList.get(0);
			String tempId2 = onlineList.get(1);
			if (tempId1.equals(senderId)) {
				receiverId = tempId2;
			} else if (tempId2.equals(senderId)) {
				receiverId = tempId1;
			}
		} else {
			receiverId = "master";
		}
		System.out.println("메세지 : payload >>> " + payload);

		try {
			dataMap = jsonToMap(payload);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		dataMap.put("senderId", senderId);
		dataMap.put("receiverId", receiverId);
		dataMap.put("time", time);
		dataMap.put("onlineList", onlineList);

		log.info("handleTextMessage final dataMap >>> " + dataMap);
		try {
			// 메세지 보내기
			System.out.println("받는사람 세션 : receiver session >>> " + userSession.get(receiverId));
			String msg;
			// 상대아이디가 존재할 때, 상대에게 보내기
			msg = json.writeValueAsString(dataMap);
			if (userSession.get(receiverId) != null && !senderId.equals(receiverId)) {
				userSession.get(receiverId).sendMessage(new TextMessage(msg));
			}
			// 내아이디와 상대아이디 다를 때 세션에 메세지 보내기
			if (!senderId.equals(receiverId)) {
				msg = json.writeValueAsString(dataMap);
				session.sendMessage(new TextMessage(msg));
			}
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// websocket에 session이 접속을 해제 했을 때, 처리하는 메소드
	// 클라이언트 연결 종료 : 세션 해제, 관련 정보 삭제
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		User loginUser = (User) session.getAttributes().get("loginUser");
		String senderId = loginUser.getUserId();
		// 저장되었던 세션 관련 정보 삭제
		sessionList.remove(session);
		onlineList.remove(senderId);
		userSession.remove(senderId);
		log.info(session + "세션 접속 해제");
		log.info(senderId + " 님이 퇴장했습니다.>>>>>>>>>>>>>>>>>>>>>");

	}

	// JSON 을 맵 형태로 변환
	public Map<String, Object> jsonToMap(String jsonString) throws JsonMappingException, JsonProcessingException {
		Map<String, Object> map = new HashMap<>();
		ObjectMapper jmapper = new ObjectMapper();
		map = jmapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
		});

		return map;
	}

	// 모두에게 채팅 보내기
	public void sendToAll(TextMessage message, String senderId) throws Exception {
		Map<String, Object> dataMap = new HashMap<>();
		// 보내는 시간
		LocalDateTime currentTime = LocalDateTime.now();
		String time = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a E"));
		// 메세지 데이터
		String payload = message.getPayload();

		log.info("sendToAll payload >>> " + payload);
		// 받는 사람
		String receiverId = "";
		if (onlineList.size() >= 2) {
			String tempId1 = onlineList.get(0);
			String tempId2 = onlineList.get(1);
			if (tempId1.equals(senderId)) {
				receiverId = tempId2;
			} else if (tempId2.equals(senderId)) {
				receiverId = tempId1;
			}
		} else {
			receiverId = "master";
		}

		dataMap.put("message", message.getPayload());
		dataMap.put("senderId", senderId);
		dataMap.put("receiverId", receiverId);
		dataMap.put("time", time);
		dataMap.put("onlineList", onlineList); // user online status
		dataMap.put("newOne", senderId);

		log.info("final dataMap >>> " + dataMap);
		log.info("receiver session >>> " + userSession.get(receiverId));

		// 세션맵에 들어있는 사람들에게 메세지 보내기
		for (String r : userSession.keySet()) {
			String msg = json.writeValueAsString(dataMap);
			userSession.get(r).sendMessage(new TextMessage(msg));
		}
	}

	// 현재 세션에 있는 사람들 리스트
	public void sendOnlineList(WebSocketSession session) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("onlineList", onlineList);
		String list = json.writeValueAsString(map);

		log.info("map >>> " + map);
		session.sendMessage(new TextMessage(list));
	}
}
