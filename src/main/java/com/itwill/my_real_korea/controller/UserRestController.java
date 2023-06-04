package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.dto.user.UserAddInfo;
import com.itwill.my_real_korea.exception.ExistedUserException;
import com.itwill.my_real_korea.exception.PasswordMismatchException;
import com.itwill.my_real_korea.exception.UserNotFoundException;
import com.itwill.my_real_korea.service.user.UserAddInfoService;
import com.itwill.my_real_korea.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserAddInfoService userAddInfoService;

	// 관리자 페이지 회원 목록
	@AdminCheck
	@LoginCheck
	@GetMapping(value = "user-admin", produces = "application/json;charset=UTF-8")
	public Response user_admin() throws Exception {
		Response response = new Response();
		try {
			List<User> userList = userService.findUserList();
			response.setStatus(1);
			response.setMessage("성공");
			response.setData(userList);
		} catch (Exception e) {
			response.setStatus(0);
			response.setMessage(e.getMessage());
			response.setData(null);
		}
		return response;
	}

	// 관리자 페이지 회원 탈퇴
	@AdminCheck
	@ResponseBody
	@PostMapping(value = "/user-admin/remove", produces = "application/json;charset=UTF-8")
	public Response removeUser(@RequestParam("userId") String userId) {
		Response response = new Response();
		try {
			userService.remove(userId);
			System.out.println(">>> remove userId : " + userId);
			response.setStatus(1);
			response.setMessage("성공");
			response.setData(null);
		} catch (Exception e) {
			response.setStatus(0);
			response.setMessage("실패");
			response.setData(null);
		}
		return response;
	}

	// 회원 가입 액션
	@ApiOperation(value = "회원 가입 액션")
	@PostMapping(value = "/user-write-action", produces = "application/json;charset=UTF-8")
	public Response user_write_action(@RequestBody User user) throws Exception {
		Response response = new Response();
		try {
			// 비밀번호 유효성 검사
			if (!userService.validatePassword(user.getPassword())) {
				response.setStatus(2);
				response.setMessage("비밀번호 형식을 확인해주세요.");
				return response;
			} else {
				userService.create(user);
				userService.updateMailKey(user);
				response.setStatus(0);
				response.setMessage("My Real Korea 회원이 되신것을 환영합니다.");
			}
		} catch (ExistedUserException e) {
			response.setStatus(3);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(4);
			response.setMessage("오류 발생");
		}
		return response;
	}

	// 로그인 액션
	@ApiOperation(value = "로그인 액션")
	@PostMapping(value = "user-login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Response user_login_action(@RequestBody User user, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Response response = new Response();
		try {
			User authUser = userService.login(user.getUserId(), user.getPassword());
			if (authUser.getMailAuth() != 1) {
				session.setAttribute("authUser", authUser);
				response.setStatus(1);
				response.setMessage("이메일을 확인해주세요.");
				response.setData("user-auth");
				System.out.println(">> user-auth (1) :" + response.getStatus());
			} else {
				User loginUser = userService.login(user.getUserId(), user.getPassword());
				session.setAttribute("loginUser", loginUser);
				String prevPage = (String) session.getAttribute("prevPage");
				System.out.println(">> prevPage : " + prevPage);
				if (prevPage == null || prevPage.contains("/user-login") || prevPage.contains("/user-auth")) {
					prevPage = request.getContextPath() + "/index";
				}
				session.removeAttribute("prevPage");
				response.setStatus(0);
				response.setMessage("로그인 성공");
				response.setData(prevPage);
				System.out.println(">> 로그인 성공 (0) :" + response.getStatus());
			}
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			response.setStatus(2);
			response.setMessage(e.getMessage());
			response.setData("user-login");
			System.out.println(">> 존재하지 않는 회원 (2) :" + response.getStatus());
			return response;
		} catch (PasswordMismatchException e) {
			e.printStackTrace();
			response.setStatus(3);
			response.setMessage(e.getMessage());
			response.setData("user-login");
			System.out.println(">> 비밀번호 불일치 (3) :" + response.getStatus());
			return response;
		}
		return response;
	}

	// 회원 인증 폼 (이메일로 전송된 인증코드)
	@LoginCheck
	@GetMapping(value = "/user-auth", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Response getUserAuth(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Response response = new Response();

		User authUser = (User) session.getAttribute("authUser");
		System.out.println(">> user_auth > authUser : " + authUser);
		if (authUser == null) {
			// authUser 객체가 존재하지 않는 경우, 로그인 페이지로 이동
			response.setStatus(1);
			response.setMessage("로그인 페이지로 이동합니다");
			response.setData("index");
		}
		authUser = userService.findUser(authUser.getUserId());
		session.setAttribute("authUser", authUser);
		response.setStatus(0);
		response.setMessage("인증 페이지로 이동합니다.");
		response.setData("user-auth");

		return response;
	}

	// 회원 인증 액션
	@PostMapping(value = "user-auth-action", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Response userAuthAction(@RequestParam("mailAuthKey") String mailAuthKey, HttpSession session) {
		Response response = new Response();
		try {
			User authUser = (User) session.getAttribute("authUser");
			if (authUser.getMailKey() == Integer.parseInt(mailAuthKey)) {
				userService.updateMailAuth(authUser);
				// 인증 완료 후 세션에서 삭제
				session.removeAttribute("authUser");
				response.setStatus(0);
				response.setData("user-login");
			} else {
				response.setStatus(1);
				response.setData("user-auth");
			}
		} catch (Exception e) {
			response.setStatus(2);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	/*************************** ID, Password 찾기 ********************************/

	// 아이디 찾기 액션
	@PostMapping(value = "/user-find-id-action", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Response user_find_id_action(@RequestParam("name") String name, @RequestParam("email") String email)
			throws Exception {
		Response response = new Response();
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		String userId = userService.findIdByEmailName(email, name);
		if (userId != null) {
			response.setStatus(1);
			response.setMessage("회원님의 아이디는 " + userId + " 입니다.");
			response.setData(userId);
		} else {
			response.setStatus(2);
			response.setMessage("일치하는 회원 정보가 없습니다.");
		}
		return response;
	}

	// 비밀번호 찾기 액션 (이메일로 임시 비밀번호 발송)
	@PostMapping(value = "/user-find-pw-action", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Response user_find_pw_action(@RequestParam("userId") String userId, @RequestParam("email") String email)
			throws Exception {
		User user = new User();
		user.setUserId(userId);
		user.setEmail(email);
		int matchCount = userService.findIdByIdEmail(userId, email);
		Response response = new Response();
		if (matchCount == 1) {
			userService.sendTempPassword(userId, email);
			response.setStatus(1);
			response.setMessage("이메일로 임시 비밀번호가 발송되었습니다.");
		} else {
			response.setStatus(2);
			response.setMessage("일치하는 회원 정보가 없습니다.");
		}
		return response;
	}

	/*********************************************************/

	/*
	 * 보류
	 * 
	 * //회원 정보 보기
	 * 
	 * @LoginCheck
	 * 
	 * @GetMapping(value = "/user-view", produces =
	 * "application/json;charset=UTF-8") public Response
	 * user_view(@RequestParam(required = false) String userId, HttpServletRequest
	 * request) throws Exception { Response response = new Response(); HttpSession
	 * session = request.getSession();
	 * 
	 * User loginUser = (User) session.getAttribute("loginUser"); if (loginUser ==
	 * null) { session.setAttribute("requestUrl",
	 * request.getRequestURL().toString()); response.setStatus(2);
	 * response.setMessage("로그인이 필요한 기능입니다."); } else { if (userId == null) { userId
	 * = loginUser.getUserId(); } User user = userService.findUser(userId); if (user
	 * == null) { response.setStatus(2); response.setMessage("해당 사용자가 존재하지 않습니다.");
	 * } else { response.setStatus(1); response.setMessage("성공");
	 * response.setData(user); } } return response; }
	 */

	@ApiOperation(value = "내 프로필 수정 폼 불러오기")
	@LoginCheck
	@GetMapping("/user-modify-form")
	public Map<String, Object> user_modify_form(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		int code = 1;
		String msg = "성공";
		List<User> data = new ArrayList<User>();

		try {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute("loginUser");
			loginUser = userService.findUser(loginUser.getUserId());
			code = 1;
			msg = "성공";
			data.add(loginUser);

		} catch (Exception e) {
			code = 2;
			msg = "실패";
			e.printStackTrace();
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 프로필 수정 액션
	@ApiOperation(value = "내 프로필 수정 액션")
	@LoginCheck
	@PutMapping("/user-modify-form-action")
	public Map<String, Object> user_modify_action(@ModelAttribute User user, HttpServletRequest request,
			@ModelAttribute UserAddInfo userAddInfo) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		int code = 1;
		String msg = "성공";
		List<User> data = new ArrayList<User>();

		try {
			HttpSession session = request.getSession();
			User loginUser = userService.findUser(user.getUserId());
			userService.update(user);
			userAddInfoService.updateUserAddInfo(userAddInfo);
			session.setAttribute("loginUser", loginUser);

		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "업로드 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

}
