package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.If;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.chat.ChatService;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2 // log 변수를 사용하여 로그 기록 가능
public class ChatController {

	@Autowired
	private ChatService chatService;

	@LoginCheck
	@GetMapping("/chat")
	public String getChat(HttpServletRequest request, Model model,
			@RequestParam(required = true, value = "receiverId") String receiverId,
			@RequestParam(required = false, value = "chatRoomName") String chatRoomName) {

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		String userId = loginUser.getUserId();
		String senderId = userId;
		String roomName = "";
		/*
		 * receiverId는 채팅 시작 클릭버튼에 hidden으로 해당 프로필유저의 아이디걸어놓은거 가져오기
		 */
		if (session != null) {
			String name = session.toString().substring(session.toString().indexOf("@"));
			session.setAttribute("sessionId", name);
		}
		// 채팅방 이름 생성
		roomName = senderId + "&" + receiverId;

		ChatRoom thisChatRoom = chatService.selectByRoomName(roomName);
		if (thisChatRoom != null) {
			roomName = thisChatRoom.getRoomName();
		} else {
			// receiverId 포함된 채팅방 리스트
			List<ChatRoom> findRoomListWithR = chatService.selectByRoomNameWith(receiverId);
			// receiverId 포함된 채팅방 중에 senderId 를 포함하는 방 찾기
			if (findRoomListWithR.size() > 0) {
				for (ChatRoom chatRoom : findRoomListWithR) {
					if (chatRoom.getRoomName().contains(senderId)) {
						ChatRoom findRoomWithRS = chatService.selectByRoomName(chatRoom.getRoomName());
						roomName = findRoomWithRS.getRoomName();
					} else {
						// 채팅방 중에 senderId를 함께 포함하는 채팅방 존재 X, 새로운 방 생성
						ChatRoom newChatRoom = new ChatRoom(roomName);
						chatService.insertChatRoom(newChatRoom);
						roomName = newChatRoom.getRoomName();
					}
				}
			} else {
				// receiverId 포함하는 채팅방 존재 X, 새로운 방 생성
				ChatRoom newChatRoom = new ChatRoom(roomName);
				chatService.insertChatRoom(newChatRoom);
				roomName = newChatRoom.getRoomName();
			}
		}

		// 채팅방 이름 파라미터로 넣어줬을 때는 그걸로 roomName 설정
		if (chatRoomName != null) {
			ChatRoom findChatRoom = chatService.selectByRoomName(chatRoomName);
			roomName = findChatRoom.getRoomName();
		}
		// senderId 가 포함된 이름의 채팅방들
		List<ChatRoom> myChatRoomList = chatService.selectByRoomNameWith(senderId);
		// senderId 가 포함된 이름의 채팅방 이름들
		List<String> myChatRoomNameList = new ArrayList<>();

		if (myChatRoomList.size() != 0) {
			for (ChatRoom chatRoom : myChatRoomList) {
				myChatRoomNameList.add(chatRoom.getRoomName());
			}
			// senderId 가 포함된 이름의 채팅방들
			model.addAttribute("myChatRoomList", myChatRoomList);
			// senderId 가 포함된 이름의 채팅방 이름들
			model.addAttribute("myChatRoomNameList", myChatRoomNameList);
			// senderId 가 포함된 이름의 채팅방 이름들 JSON으로 직렬화
			JSONArray jsonArray = new JSONArray(myChatRoomNameList);
			String jsonMyChatRoomNameList = jsonArray.toString();
			model.addAttribute("jsonMyChatRoomNameList", jsonMyChatRoomNameList);

			System.out.println(">>>>>>>>>>>>>>>>myChatRoomNameList" + myChatRoomNameList);
		}
		System.out.println(">>>> roomName :" + roomName);

		model.addAttribute("receiverId", receiverId);
		model.addAttribute("senderId", senderId);
		model.addAttribute("roomName", roomName);
		// 세션에 채팅방 이름 저장
		session.setAttribute("chatRoomName", roomName);

		log.info("@ChatController, getChat()");
		return "chat";
	}

	@GetMapping("/chat/master")
	public String enterChatAsMaster(HttpServletRequest request) {

		log.info("@ChatController, enterChatAsMaster()");
		return "/chat";
	}

}
