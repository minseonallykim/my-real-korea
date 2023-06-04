package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itwill.my_real_korea.dto.freeboard.FreeBoard;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.ticket.TicketReview;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourReview;
import com.itwill.my_real_korea.service.freeboard.FreeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.Payment;
import com.itwill.my_real_korea.dto.freeboard.FreeBoard;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.dto.user.KakaoProfile;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.freeboard.FreeBoardService;
import com.itwill.my_real_korea.service.payment.PaymentService;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.ticket.TicketReviewService;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.service.tour.TourReviewService;
import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.service.tripboard.TripBoardService;
import com.itwill.my_real_korea.service.user.KaKaoService;
import com.itwill.my_real_korea.service.user.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private KaKaoService kakaoService;
	@Autowired
	private CityService cityService;
	@Autowired
	private TripBoardService tripBoardService;
	@Autowired
	private FreeBoardService freeBoardService;
	@Autowired
	private TourReviewService tourReviewService;
	@Autowired
	private TicketReviewService ticketReviewService;
	@Autowired
	private TourService tourService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketImgService ticketImgService;

	// 회원 가입 폼
	@GetMapping(value = "/user-write", produces = "application/json;charset=UTF-8")
	public String user_write() {
		return "user-write";
	}

	// 아이디 중복 체크
	@PostMapping("/idCheck")
	@ResponseBody
	public int countExistId(@RequestParam("userId") String userId) throws Exception {
		int existCount = userService.countExistId(userId);
		return existCount;
	}

	/*************************** 카카오 로그인 *****************************/

	@ResponseBody
	@GetMapping("/kakao_userinfo_with_token")
	public KakaoProfile getKakaoUserInfoWithToken(String access_token, HttpSession session) throws Exception {
		System.out.println(">>> getKakaoUserInfoWithToken access_token : " + access_token);
		KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(access_token);
		System.out.println(">>> getKakaoUserInfoWithToken kakaoProfile : " + kakaoProfile);

		return kakaoProfile;
	}

	@RequestMapping(value = "/kakao-login", method = RequestMethod.GET)
	public String kakao_login_action(@RequestParam(value = "code", required = false) String code,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String access_token = kakaoService.getToken(code);
		System.out.println(">>> login_action access_token:" + access_token);
		KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(access_token);

		// 로그인 처리 전에 데이터베이스에서 사용자 정보 조회
		String userId = "K_" + kakaoProfile.getId();
		String password = "k" + kakaoProfile.getId() + "!";
		String name = kakaoProfile.getProperties().getNickname();
		String nickname = "kakao_" + kakaoProfile.getProperties().getNickname();
		String email = kakaoProfile.getKakao_account().getEmail();
		int gender = 0;
		if (kakaoProfile.getKakao_account().getGender().equals("male")) {
			gender = 1;
		} else if (kakaoProfile.getKakao_account().getGender().equals("female")) {
			gender = 2;
		} else {
			gender = 0;
		}

		System.out.println(">>> login_action userId : " + userId);
		User kakaoUser = userService.findUser(userId);
		System.out.println(">>> login_action kakaoUser : " + kakaoUser);

		HttpSession session = request.getSession();
		session.invalidate();

		session = request.getSession();
		// 회원으로 등록되어 있으면 로그인, 그렇지 않으면 회원 가입
		if (kakaoUser != null) {
			User loginUser = userService.findUser(kakaoUser.getUserId());
			session.setAttribute("loginUser", loginUser);
		} else {
			User newUser = new User(userId, password, name, nickname, " ", email, new Date(), "제주", gender, 0, 1, 1, 0);
			userService.create(newUser);
			session.setAttribute("user_id", userId);
		}

		request.setAttribute("kakaoProfile", kakaoProfile);
		Cookie authorize_access_token = new Cookie("authorize-access-token", access_token);
		response.addCookie(authorize_access_token);

		return "index";
	}

	/******************************************************************************/

	// 로그인 폼
	@GetMapping(value = "/user-login", produces = "application/json;charset=UTF-8")
	public String user_login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();

		String prevPage = request.getHeader("Referer");
		if (prevPage == null || prevPage.contains("/user-login") || prevPage.contains("/user-auth")) {
			prevPage = request.getContextPath() + "/index";
		}
		session.setAttribute("prevPage", prevPage);
		model.addAttribute("prevPage", prevPage);
		return "user-login";
	}

	/*************************** ID, Password 찾기 ********************************/

	// 아이디, 비밀번호 찾기 폼
	@GetMapping(value = "/user-find", produces = "application/json;charset=UTF-8")
	public String user_find() {
		return "user-find";
	}

	// 회원 정보 보기
	@LoginCheck
	@GetMapping(value = "/user-view", produces = "application/json;charset=UTF-8")
	public String user_view(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		System.out.println(">> loginUser : " + loginUser);
		if (loginUser == null) {
			session.setAttribute("requestUrl", request.getRequestURL().toString());
			return "redirect:user-login";
		}
		loginUser = userService.findUser(loginUser.getUserId());
		request.setAttribute("loginUser", loginUser);
		System.out.println(loginUser);

		// 도시리스트 붙이기
		List<City> cityList = cityService.findAllCity();
		request.setAttribute("cityList", cityList);

		// 1. 상세페이지 예약내역
		List<Payment> paymentList = paymentService.selectAllUser(loginUser.getUserId());
		request.setAttribute("paymentList", paymentList);

		// 2. 마이페이지 내가 쓴 동행게시판 게시글
		List<TripBoard> tripBoardList = tripBoardService.selectAllUser(loginUser.getUserId());
		request.setAttribute("tripBoardList", tripBoardList);
		System.out.println(tripBoardList);

		// 3. 마이페이지 내가 쓴 자유게시판 게시글
		List<FreeBoard> freeBoardList = freeBoardService.selectByUserId(loginUser.getUserId());
		request.setAttribute("freeBoardList", freeBoardList);

		// 4. 내 리뷰
		List<TourReview> tempTourReviewList = tourReviewService.findByUserId(loginUser.getUserId());
		List<TourReview> tourReviewList = new ArrayList<>();
		for (TourReview tourReview : tempTourReviewList) {
			Tour tour = tourService.findTourWithCityByToNo(tourReview.getToNo());
			tourReview.setTour(tour);
			tourReviewList.add(tourReview);
		}
		List<TicketReview> tempTicketReviewList = ticketReviewService.selectByTicketReviewUser(loginUser.getUserId());
		List<TicketReview> ticketReviewList = new ArrayList<>();
		for (TicketReview ticketReview : tempTicketReviewList) {
			Ticket ticket = ticketService.selectTicketNo(ticketReview.getTiNo());
			List<TicketImg> ticketImgList = ticketImgService.selectTicketImgList(ticketReview.getTiNo());
			ticket.setTicketImgList(ticketImgList);
			ticketReview.setTicket(ticket);
			ticketReviewList.add(ticketReview);
		}
		System.out.println(tourReviewList);
		System.out.println(ticketReviewList);
		request.setAttribute("tourReviewList", tourReviewList);
		request.setAttribute("ticketReviewList", ticketReviewList);

		return "user-view";
	}

	// 회원 정보 수정 폼
	@LoginCheck
	@PostMapping("/user-modify-plain")
	public String user_modify(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		loginUser = userService.findUser(loginUser.getUserId());
		request.setAttribute("loginUser", loginUser);
		return "user-modify-plain";
	}

	// 회원 정보 수정 액션
	@LoginCheck
	@PostMapping("user-modify-action-plain")
	public String user_modify_action(@ModelAttribute User user, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		userService.update(user);
		User loginUser = userService.findUser(user.getUserId());
		session.setAttribute("loginUser", loginUser);
		return "redirect:user-view";
	}

	// 회원 탈퇴 액션
	@LoginCheck
	@PostMapping("user-remove-action")
	public String user_remove_action(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		userService.remove(loginUser.getUserId());
		request.getSession(false).invalidate();
		return "redirect:index";
	}

	// 로그아웃 액션
	@LoginCheck
	@RequestMapping("user-logout-action")
	public String user_logout_action(HttpServletRequest request) {
		request.getSession(false).invalidate();
		return "redirect:index";
	}

	// GET 방식으로 요청시 index 화면으로
	@GetMapping({ "user-write-action", "user-login-action", "user-modify", "user-modify-action", "user-remove-action" })
	public String user_get() {
		return "redirect:index";
	}

	// error 발생시 error 화면으로
	@ExceptionHandler(Exception.class)
	public String user_exception_handler(Exception e) {
		return "error";
	}

}
