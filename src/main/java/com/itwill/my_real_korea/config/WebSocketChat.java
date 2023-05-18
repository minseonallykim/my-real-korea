package com.itwill.my_real_korea.config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.itwill.my_real_korea.dto.chat.ChatMsg;

import lombok.extern.log4j.Log4j2;

public class WebSocketChat {
	
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

	private static Map<String, Session> userSessions = new HashMap();
	
	String userId = "";
	String userKey = "";
	
	// 웹소켓 연결 시
	public void onOpen(Session session) {
		String userId = "";
		String roomNo = "";
		String queryStr = (String)session.getQueryString();
		String key= "";
		// 세션의 URI 쿼리스트링에서 chatID와 roomNo 얻기
		if(queryStr.contains("&")) {
			String[] qStrArray = queryStr.split("&");
			userId = qStrArray[0].trim();
			roomNo = qStrArray[1].trim();
			key = userId + "&"+ roomNo;
		}else {
			userId = (String)session.getQueryString();
			key = userId;
		}
		// userKey = key = 채팅아이디&채팅방번호 or 채팅아이디
		this.userKey = key;
		
		System.out.println("연결 세션:" + session);
		System.out.println("채팅아이디:" + userId);
		// key String값에 session 넣기
		userSessions.put(key, session);
		System.out.println(userSessions);
		
	}

	// 웹소켓 연결 끊길 때
	public void OnClose(Session session) {
		System.out.println("socket 닫기 : " + userKey);
		userSessions.remove(userKey);
		System.out.println(userSessions);
	}

	// 웹소켓 메세지 보낼 때
	public void onMessage(String message, Session session) {
		System.out.println("---------ON MESSAGE--------");
		System.out.println(message);
		System.out.println("메세지 전송한 세션:" + session);

		// 메세지로 발신자, 수신자 확인
		JSONObject jsonObj = new JSONObject(message);
		String code = jsonObj.getString("code");
		String receiverId = jsonObj.getString("receiverId");
		String userId = jsonObj.getString("userId");
		
		System.out.println("메세지(JSONObject) 보내는 내 아이디(key):" + userId);
		System.out.println("메세지(JSONObject) 받는 상대 아이디(key):" + receiverId);

		JSONArray jsonArr = (JSONArray) jsonObj.get("data");
		JSONObject jsonChat = (JSONObject) jsonArr.get(0);
		Session myChatSession = userSessions.get(userId + "&" + jsonChat.getString("roomName"));

		System.out.println("메세지 보내는 세션:" + myChatSession);
		System.out.println("jsonData.data 채팅 내용:" + jsonChat.getString("msgContent"));
		
		ChatMsg newChat;
		try {
			newChat = new ChatMsg(0, jsonChat.getString("msgContent"), 
										jsonChat.getString("msgSendTime"),
										Integer.parseInt(jsonChat.getString("msgRead")), 
										jsonChat.getString(jsonChat.getString("roomName")),
										jsonChat.getString("userId"));
			System.out.println("채팅 DB 넣을 때 객체:" + newChat);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		} 
		// code 가 1 일 때 session을 돌면서 메세지 돌리기(메세지 전송한 경우)
		if (code.equals("1")) {
			jsonChat.put("code", 1);

			try {
				Session yourSession = userSessions.get(receiverId + "&" + jsonChat.getString("roomNo"));
				System.out.println("메세지 받는 세션:" + yourSession);
				System.out.println("채팅 상대방 소켓에 메세지 전송 시도");

				if (yourSession != null && myChatSession != null) {
					jsonChat.put("toastId", "youExist");
					jsonChat.put("msgRead", 1);
					yourSession.getBasicRemote().sendText(jsonChat.toString());
					myChatSession.getBasicRemote().sendText(jsonChat.toString());
				} else if (yourSession != null) {
					myChatSession.getBasicRemote().sendText(jsonChat.toString());
				} else {
					for (String key : userSessions.keySet()) {
						jsonChat.put("toastId", receiverId);
						Session s = userSessions.get(key);

						System.out.println("일반 알람 전송 시도 :" + s);
						s.getBasicRemote().sendText(jsonChat.toString());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		// code 가 2 일 때 (채팅방 입장한 경우)
		} else if (code.equals("2")) {
			jsonChat.put("code", 2);
			try {
				Session yourSession = userSessions.get(receiverId + "&" + jsonChat.getString("roomNo"));
				System.out.println("메세지 받는 세션:" + yourSession);

				System.out.println(">>>채팅 상대방 소켓에 입장 시도");
				if (yourSession != null && myChatSession != null) {
					System.out.println(">>>한 명 접속한 상태에서 새로운 접속 시도");
					jsonChat.put("msgRead", 1);
					yourSession.getBasicRemote().sendText(jsonChat.toString());
					myChatSession.getBasicRemote().sendText(jsonChat.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
}