package com.itwill.my_real_korea.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.handler.ChatHandler;
import com.itwill.my_real_korea.service.chat.ChatService;

import ch.qos.logback.core.joran.conditional.IfAction;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class ChatRestController {

	@Autowired
	private ChatService chatService;

	// 채팅 내용 불러오기
	@PostMapping(value = "/chat-detail-rest")
	public Map<String, Object> chatDetailRest(@RequestBody Map<String, String> chatList) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "";
		String roomName = chatList.get("roomName");
		String senderId = chatList.get("senderId");
		String receiverId = chatList.get("receiverId");
		// 채팅방 번호로 채팅방 찾기
		ChatRoom chatRoom = chatService.selectByRoomName(roomName);
		String findChatRoomName = chatRoom.getRoomName();

		List<ChatMsg> resultList = new ArrayList<ChatMsg>();
		try {
			// 해당 채팅방 채팅 내역 찾기
			List<ChatMsg> chatDetailList = chatService.selectChatByRoomName(findChatRoomName);
			if (chatDetailList.size() > 0) {
				code = 1;
				msg = "성공";
				resultList = chatDetailList;
				System.out.println("chatDetailList" + chatDetailList);
			} else {
				code = 2;
				msg = "채팅내용이 없습니다.";
			}

		} catch (Exception e) {
			code = 3;
			msg = "관리자에게 문의하세요";
			e.printStackTrace();
		}
		System.out.println("채팅아이디" + senderId);
		System.out.println("상대아이디" + receiverId);

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("senderId", senderId);
		resultMap.put("receiverId", receiverId);
		resultMap.put("roomName", findChatRoomName);
		resultMap.put("data", resultList);

		return resultMap;
	}
	// 메세지 보내기 & DB저장
	@PostMapping(value = "/save-chat")
	public Map<String, Object> saveChat(@RequestBody Map<String, String> messages) {

		Map<String, Object> resultMap = new HashMap<>();
		List<ChatMsg> data = new ArrayList<>();
		int code = 1;
		String msg = "";
		int msgNo = 0;
		String msgContent = "";
		String msgSendTime = "";
		int msgRead = 0;
		String roomName = "";
		String senderId = "";
		String myId = "";

		try {
			msgContent = String.valueOf(messages.get("message"));
			msgSendTime = String.valueOf(messages.get("time"));
			msgRead = 0;
			roomName = String.valueOf(messages.get("roomName"));
			senderId = String.valueOf(messages.get("senderId"));
			myId = String.valueOf(messages.get("myId"));
			System.out.println(msgContent);
			System.out.println(msgSendTime);
			System.out.println(msgRead);
			System.out.println(roomName);
			System.out.println(senderId);
			// 채팅방 존재여부 판별
			ChatRoom findChatRoom = chatService.selectByRoomName(roomName);
			if (findChatRoom != null) {
				// 로그인한 아이디와 채팅보내는 아이디 같을 때만 채팅 저장
				if (myId.equals(senderId)) {
					ChatMsg newChatMsg;
					newChatMsg = new ChatMsg(0, msgContent, msgSendTime, msgRead, roomName, senderId);
					// 메세지 생성, DB 저장
					int rowCount = chatService.insertChatMsg(newChatMsg);
					if (rowCount > 0) {
						code = 1;
						msg = "성공";
						data.add(newChatMsg);
					}
				}
			} else {
				code = 3;
				msg = "채팅방 존재하지 않습니다.";
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			code = 2;
			msg = "메세지 DB저장 실패";
			e.printStackTrace();
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * chatRoom
	 */

	// 채팅방 목록 보기 - 성공
	@LoginCheck
	@ApiOperation(value = "채팅방 리스트")
	@GetMapping(value = "/chatroom", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatroom_list(@RequestParam(required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatRoom> data = new ArrayList<>();
		try {
			// userId로 채팅방 리스트 찾기, 성공시 code 1
			data = chatService.selectAll(userId);
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			// 에러 발생시 code 2
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 채팅방 목록 선택 기능 (roomNo로 채팅방 1개 보기) - session 빼고 성공
	@LoginCheck
	@ApiOperation(value = "채팅방 상세보기")
	@ApiImplicitParam(name = "roomName", value = "채팅방 번호")
	@GetMapping(value = "/chatroom/{roomName}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatroom_detail(@PathVariable(value = "roomName") String roomName, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatRoom> data = new ArrayList<>();

		try {
			// roomNo로 채팅방 1개 찾기, 성공시 code 1
			ChatRoom chatRoom = chatService.selectByRoomName(roomName);
			// 요청한 userId : session에서 찾기
			String userId = (String) session.getAttribute("sUserId");
			if (chatRoom != null) {
				code = 1;
				data.add(chatRoom);
			} else {
				// 실패 시 code 2
				code = 2;
				msg = "해당 채팅방이 존재하지 않습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 에러 발생 시 code 3
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 채팅방 생성 - 웹소켓 연결로 스웨거 테스트 안됨
	@LoginCheck
	@ApiOperation(value = "채팅방 생성")
	@PostMapping(value = "/chatroom", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatroom_create_action(@RequestBody ChatRoom chatRoom, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatRoom> data = new ArrayList<>();

		// 채팅방 생성 요청하는 유저
		String fromId = (String) session.getAttribute("sUserId");
		try {
			// 채팅방 생성, 성공시 code 1
			chatService.insertChatRoom(chatRoom);
			code = 1;
			msg = "성공";
			// 채팅방 생성 후 그 채팅방 찾아서 데이터에 붙여줌
			chatRoom = chatService.selectByRoomName(chatRoom.getRoomName());
			data.add(chatRoom);
		} catch (Exception e) {
			// 실패 시 code 3
			e.printStackTrace();
			code = 3;
			msg = "채팅방 생성 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 채팅방 삭제 - 성공
	@LoginCheck
	@ApiOperation(value = "채팅방 삭제")
	@ApiImplicitParam(name = "roomName", value = "채팅방 이름")
	@DeleteMapping(value = "/chatroom/{roomName}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatroom_delete_action(@PathVariable(value = "roomName") String roomName) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatRoom> data = new ArrayList<>();

		try {
			// roomNo로 채팅방 삭제, 성공시 code 1
			int rowCount = chatService.deleteChatRoom(roomName);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "채팅방 삭제 실패";
				// 삭제 실패한 roomNo 데이터에 붙여줌
				ChatRoom failChatRoom = chatService.selectByRoomName(roomName);
				data.add(failChatRoom);
			}
		} catch (Exception e) {
			// 에러시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의바랍니다.";

		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * chatMsg
	 */
	// 채팅방 1개의 전체 대화보기 - 성공
	@LoginCheck
	@ApiOperation(value = "채팅방 1개의 메세지 리스트")
	@GetMapping(value = "/chatmsg", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatmsg_list(@RequestParam(required = true) String roomName) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatMsg> data = new ArrayList<>();
		try {
			// roomNo로 채팅방 1개의 메세지 리스트 찾기, 성공시 code 1
			List<ChatMsg> chatMsgList = chatService.selectChatByRoomName(roomName);
			if (chatMsgList.size() != 0) {
				data = chatMsgList;
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "해당 채팅방의 메세지는 존재하지 않습니다.";
			}
		} catch (Exception e) {
			// 에러 발생시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 채팅 메세지 1개 보기 - session 빼고 성공
	@LoginCheck
	@ApiOperation(value = "채팅메세지 1개 상세보기")
	@ApiImplicitParam(name = "msgNo", value = "채팅메세지 번호")
	@GetMapping(value = "/chatmsg/{msgNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatmsg_detail(@PathVariable(value = "msgNo") int msgNo, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatMsg> data = new ArrayList<>();
		try {
			// msgNo로 채팅메세지 1개 찾기, 성공시 code 1
			ChatMsg chatMsg = chatService.selectByMsgNo(msgNo);
			// 요청한 userId : session에서 찾기
			String userId = (String) session.getAttribute("sUserId");
			if (chatMsg != null) {
				code = 1;
				data.add(chatMsg);
			} else {
				// 실패 시 code 2
				code = 2;
				msg = "해당 채팅메세지가 존재하지 않습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 에러 발생 시 code 3
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 읽지 않은 메세지 전체 보기 - 성공
	@LoginCheck
	@ApiOperation(value = "읽지 않은 메세지 전체 리스트")
	@GetMapping(value = "/chatmsg-all-not-read", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatmsg_all_not_read_list(@RequestParam(required = true) String userId) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatMsg> data = new ArrayList<>();
		try {
			int notReadMsgCount = chatService.countAllNotReadMsg(userId);
			// userId로 읽지 않은 메세지 수가 0 이 아니라면, 전체 리스트 찾기, 성공시 code 1
			if (notReadMsgCount != 0) {
				data = chatService.selectAllNotReadMsg(userId);
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "읽지 않은 메세지가 없습니다.";
			}
		} catch (Exception e) {
			// 에러 발생시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 안 읽은 채팅 수
	@GetMapping(value = "/count-not-read-chat")
	public Map<String, Object> countNotReadChat(@RequestParam String userId, @RequestParam int roomNo) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		int data = 0;
		try {
			data = chatService.countNotReadInChatRoom(roomNo, userId);
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			code = 2;
			msg = "안읽은 채팅 수 불러오기 실패";
			e.printStackTrace();
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 채팅 메세지 1개 삭제 - session 빼고 성공
	@LoginCheck
	@ApiOperation(value = "채팅메세지 삭제")
	@ApiImplicitParam(name = "msgNo", value = "채팅메세지 번호")
	@DeleteMapping(value = "/chatmsg/{msgNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatmsg_delete_action(@PathVariable(value = "msgNo") int msgNo, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatMsg> data = new ArrayList<>();
		try {
			// 삭제 요청한 userId : session에서 찾기
			String userId = (String) session.getAttribute("sUserId");
			// 해당 채팅메세지를 전송했던 userId 찾기
			ChatMsg chatMsg = chatService.selectByMsgNo(msgNo);
			String chatMsgUserId = chatMsg.getUserId();

			// 채팅메세지 전송한 userId = 삭제 요청한 userId 라면 메세지 삭제 시도
			if (userId.equals(chatMsgUserId)) {
				// msgNo로 채팅메세지 삭제, 성공시 code 1
				int rowCount = chatService.deleteChatMsg(msgNo);
				if (rowCount != 0) {
					code = 1;
					msg = "성공";
				} else {
					// 실패시 code 2
					code = 2;
					msg = "채팅메세지 삭제 실패";
					// 삭제 실패한 roomNo 데이터에 붙여줌
					ChatMsg failChatMsg = chatService.selectByMsgNo(msgNo);
					data.add(failChatMsg);
				}
			} else {
				// 다른 userId가 삭제 요청할 경우 code 3
				code = 3;
				msg = "메세지 삭제가 불가능합니다.";
			}
		} catch (Exception e) {
			// 에러시 code 4
			e.printStackTrace();
			code = 4;
			msg = "관리자에게 문의바랍니다.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 채팅 메세지 생성 - 성공
	@LoginCheck
	@ApiOperation(value = "채팅메세지 생성")
	@PostMapping(value = "/chatmsg", produces = "application/json;charset=UTF-8")
	public Map<String, Object> chatmsg_create_action(@RequestBody ChatMsg chatMsg) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<ChatMsg> data = new ArrayList<>();
		try {
			// 채팅 메세지 생성 , 성공시 code 1
			chatService.insertChatMsg(chatMsg);
			code = 1;
			msg = "성공";
			// 채팅 메세지 생성 후 그 채팅방 찾아서 데이터에 붙여줌
			chatMsg = chatService.selectByMsgNo(chatMsg.getMsgNo());
			data.add(chatMsg);
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "채팅 메세지 생성 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}
}
