package com.itwill.my_real_korea.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.Payment;
import com.itwill.my_real_korea.dto.RsPInfo;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.ticket.TicketReview;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.location.LocationService;
import com.itwill.my_real_korea.service.payment.PaymentService;
import com.itwill.my_real_korea.service.rspinfo.RsPInfoService;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.ticket.TicketReviewService;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.service.user.UserService;
import com.itwill.my_real_korea.util.PageMakerDto;

@Controller
public class TicketController {

	private final TicketService ticketService;
	private final TicketReviewService ticketReviewService;
	private final PaymentService paymentService;
	private final RsPInfoService rsPInfoService;
	private final TicketImgService ticketImgService;
	private final CityService cityService;
	private final UserService userService;
	private final LocationService locationService;

	@Autowired
	public TicketController(TicketService ticketService, TicketReviewService ticketReviewService,
			RsPInfoService rsPInfoService, PaymentService paymentService, TicketImgService ticketImgService,
			CityService cityService, UserService userService,
			LocationService locationService/*
											 * , Aws3UploadService aws3UploadService
											 */) {
		this.ticketService = ticketService;
		this.ticketReviewService = ticketReviewService;
		this.paymentService = paymentService;
		this.rsPInfoService = rsPInfoService;
		this.ticketImgService = ticketImgService;
		this.cityService = cityService;
		this.userService = userService;
		this.locationService = locationService;
	}

	// 티켓 리스트 - 페이지
	@GetMapping("/ticket-list")
	public String tickeList(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "0") int cityNo,
			@RequestParam(required = false) String sortOrder, Model model, HttpSession session) {
		String forwardPath = "";
		try {
			// 위시리스트 추가 코드 시작
			// 로그인 한 유저면 userId model에 붙이기
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser != null) {
				String userId = loginUser.getUserId();
				model.addAttribute("userId", userId);
			}
			// 위시리스트 추가 코드 끝

			PageMakerDto<Ticket> ticketPage = ticketService.selectByTicketAllSort(currentPage, keyword, cityNo,
					sortOrder);
			List<Ticket> ticketList = new ArrayList<>();
			for (Ticket ticket : ticketPage.getItemList()) {
				if (ticketReviewService.selectByTicketReviewNo(ticket.getTiNo()).size() == 0) {
					ticket.setTiScore(0);
					ticketList.add(ticket);
				} else {
					int ticketScore = ticketReviewService.calculateTourScore(ticket.getTiNo());
					ticket.setTiScore(ticketScore);
					ticketList.add(ticket);
				}
			}
			ticketPage.setItemList(ticketList);
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("ticketPage", ticketPage);
			model.addAttribute("cityList", cityList);
			forwardPath = "ticket-list";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "error";
		}
		return forwardPath;
	}

	// 티켓 -- 상세보기 detail
	@GetMapping("/ticket-detail")
	public String ticketDetail(@RequestParam int tiNo, Model model, HttpSession session) {

		String forwardPath = "";
		try {
			List<Ticket> ticketList = ticketService.selectByTicketNoCityWithImg(tiNo); // 이미지 list로
			List<TicketImg> ticketImgList = ticketImgService.selectTicketImgList(tiNo);
			List<TicketReview> ticketReviewList = ticketReviewService.selectByTicketReviewNo(tiNo);
			// 위시리스트 추가 코드 시작
			// 로그인 한 유저면 userId model에 붙이기
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser != null) {
				String userId = loginUser.getUserId();
				model.addAttribute("userId", userId);
			}
			// 위시리스트 추가 코드 끝
			Ticket ticket = ticketService.selectTicketNo(tiNo);
			if (ticket != null) {
				int ticketScore = ticketReviewService.calculateTourScore(tiNo);
				ticket.setTiScore(ticketScore);
				model.addAttribute("ticket", ticket);
			}

			model.addAttribute("ticketList", ticketList); // 티켓
			model.addAttribute("ticketImgList", ticketImgList); // 티켓이미지
			model.addAttribute("ticketReviewList", ticketReviewList); // 리뷰
			model.addAttribute("tiNo", tiNo);
			// 상단 지역별 카테고리 때문에 넣음
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
			// 사진을 제외한 공통된 하나의 티켓 정보

			ticket = ticketList.get(0);
			ticket.setLocation(locationService.findByTiNo(tiNo));

			// 세션에 티켓 정보 담기
			session.setAttribute("ticketImgList", ticketImgList); // 굳이?
			session.setAttribute("ticket", ticket);
			// System.out.println(ticket);
			forwardPath = "ticket-detail";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "redirect:error";
		}
		return forwardPath;
	}

	// 티켓 상세 페이지에서 수량, 예약하기
	@LoginCheck
	@PostMapping("/ticket-detail-aciton")
	public String ticketDetailPayment(@RequestParam String pStartDate, @RequestParam int pQty, HttpSession session)
			throws Exception {
		String forwardPath = "";
		Ticket ticket = (Ticket) session.getAttribute("ticket");
		/* 세션으로 넣기 */
		User loginUser = (User) session.getAttribute("loginUser");
		session.setAttribute("loginUser", loginUser);

		try {
			// 총 금액 = 수량 * 티켓 가격
			int price = pQty * ticket.getTiPrice();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(pStartDate);
			Payment payment = new Payment();
			payment.setPPrice(price); // 총금액
			payment.setPQty(pQty); // 수량
			payment.setPPoint((int) (price * 0.01)); // 포인트
			payment.setPStartDate(date); // 예약날짜
			payment.setUserId(loginUser.getUserId()); // user 담기
			payment.setTicket(ticket); // 티켓 담기
			session.setAttribute("payment", payment);
			session.setAttribute("ticket", ticket);
			// System.out.println(payment);
			forwardPath = "ticket-payment";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "redirect:error";
		}

		return forwardPath;
	}

	// 결제 상세 페이지 - action
	@LoginCheck
	@PostMapping("/ticket-payment-complete-action") // 결제 성공 //default값 설정
	public String ticketPaymentCompleteAction(HttpSession session, @ModelAttribute RsPInfo rsPInfo,
			@RequestParam(required = false, defaultValue = "") String pMsg, @RequestParam int pMethod, // ?
			@RequestParam int pPoint, // ?
			@RequestParam int pPrice, // ?
			@RequestParam int amount, RedirectAttributes redirectAttributes) {
		String forwardPath = "";
		Payment payment = (Payment) session.getAttribute("payment");
		Ticket ticket = (Ticket) session.getAttribute("ticket");
		User loginUser = (User) session.getAttribute("loginUser");

		try {
			if (pMethod == 1) {
				payment.setPMethod(1);
			} else if (pMethod == 2) {
				payment.setPMethod(2);
			}
			payment.setPMsg(pMsg);
			payment.setPPoint(pPoint);
			payment.setPPrice(amount);
			payment.setPNo(payment.getPNo());
			int newPoint = loginUser.getPoint() + pPoint;
			loginUser.setPoint(newPoint);
			userService.updatePoint(loginUser);
			paymentService.insertTicketPayment(payment); // 확인하기!
			ticket.setTiCount(ticket.getTiCount() + payment.getPQty());
			ticketService.updateTicket(ticket);

			Ticket updateTicket = ticketService.selectTicketNo(ticket.getTiNo());
			Payment selectPayment = paymentService.findLatestPaymentByUserId(loginUser.getUserId());
			selectPayment.setTicket(updateTicket);

			rsPInfo.setPNo(selectPayment.getPNo());
			rsPInfo.setUserId(loginUser.getUserId());
			rsPInfoService.insertRsPerson(rsPInfo);

			redirectAttributes.addFlashAttribute("payment", selectPayment);
			redirectAttributes.addFlashAttribute("rsPInfo", rsPInfo);

			session.removeAttribute("payment");
			session.removeAttribute("rsPInfo");

			forwardPath = "redirect:ticket-payment-confirmation";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "redirect:error";
		}

		return forwardPath;
	}

	@LoginCheck
	@RequestMapping(value = "ticket-payment-confirmation")
	public String ticketPaymentRemove(@ModelAttribute Payment payment, @ModelAttribute RsPInfo rsPInfo, Model model) {
		String forwardPath = "";
		model.addAttribute(payment);
		model.addAttribute(rsPInfo);
		forwardPath = "ticket-payment-confirmation";
		return forwardPath;
	}

	@PostMapping(value = "/ticket-list-sort", produces = "application/json;charset=UTF-8") // rest ?
	@ResponseBody
	public Map<String, Object> ticketList(@RequestBody Map<String, String> map) {
		Map<String, Object> ticketSortMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Ticket> data = null;
		int currentPage = Integer.parseInt(map.get("currentPage"));
		int cityNo = Integer.parseInt(map.get("cityNo"));
		String keyword = map.get("keyword");
		String sortOrder = map.get("sortOrder");

		try {
			PageMakerDto<Ticket> ticketPage = ticketService.selectByTicketAllSort(currentPage, keyword, cityNo,
					sortOrder);
			List<Ticket> tempTicketList = ticketPage.getItemList();
			List<Ticket> ticketList = new ArrayList<>();
			for (Ticket ticket : tempTicketList) {
				int ticketScore = ticketReviewService.calculateTourScore(ticket.getTiNo());
				ticket.setTiScore(ticketScore);
				ticketList.add(ticket);
			}
			data = ticketList;
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}
		ticketSortMap.put("code", code);
		ticketSortMap.put("msg", msg);
		ticketSortMap.put("data", data);
		return ticketSortMap;
	}

	@LoginCheck
	@PostMapping(value = "/ticket-review-action", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> ticketReviewAction(@RequestBody TicketReview ticketReview, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		String msg = "성공";

		List<TicketReview> data = new ArrayList<>();
		try {
			User loginUser = (User) session.getAttribute("loginUser");
			ticketReview.setUser(loginUser);
			ticketReview.setTiReviewImg(ticketReview.getTiReviewImg());
			ticketReviewService.insertTicketReview(ticketReview);
			// 티켓에 리뷰 붙이기
			ticketReview = ticketReviewService.selectByTicketReviewOne(ticketReview.getTiReviewNo());
			data.add(ticketReview);
			code = 1;
			msg = "성공";

		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요";
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	/* 기존 내용 가져오기 */
	@GetMapping(value = "ticket-detail/ticketReview/{tiReviewNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> ticketReviewModify(@PathVariable("modifyTiReviewNo") int tiReviewNo) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "";
		List<TicketReview> data = new ArrayList<>();
		TicketReview ticketReview = ticketReviewService.selectByTicketReviewOne(tiReviewNo);
		if (ticketReview != null) {
			data.add(ticketReview);
		} else {
			code = 2;
			msg = "게시물 존재이 존재하지 않습니다.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	@LoginCheck
	@ResponseBody
	@PutMapping(value = "/ticketReview/{tiReviewNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> ticketReviewModifyAction(@PathVariable(value = "tiReviewNo") int tiReviewNo,
			@RequestBody TicketReview ticketReview, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		String msg = "성공";
		List<TicketReview> data = new ArrayList<>();

		try {
			TicketReview findTicketReview = ticketReviewService.selectByTicketReviewOne(tiReviewNo); // tiNo에 있는
																										// ticketReview
																										// 한 개.
			if (findTicketReview != null) {
				ticketReviewService.updateTicketReview(ticketReview);
				code = 1;
				msg = "성공";
				data.add(ticketReview);
			} else {
				code = 2;
				msg = "리뷰 사진 수정 실패";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	@LoginCheck
	@ResponseBody
	// @LoginCheck // 내가 써야 삭제
	@DeleteMapping(value = "/ticketReview/{tiReviewNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> ticketReviewDeleteAction(@PathVariable(value = "tiReviewNo") int tiReivewNo,
			HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		String msg = "성공";
		List<TicketReview> data = new ArrayList<>();

		try {
			int rowCount = ticketReviewService.deleteTicketReview(tiReivewNo);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "삭제 실패";
				TicketReview failTicketReview = ticketReviewService.selectByTicketReviewOne(tiReivewNo);
				data.add(failTicketReview);
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의바랍니다.";
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

}
