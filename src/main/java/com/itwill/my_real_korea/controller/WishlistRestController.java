package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.service.tour.TourImgService;
import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.service.wishlist.WishlistService;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class WishlistRestController {

	@Autowired
	private WishlistService wishlistService;
	@Autowired
	private TourImgService tourImgService;
	@Autowired
	private TicketImgService ticketImgService;

	/*
	 * 티켓 상품 위시리스트에 추가
	 */
	@LoginCheck
	@ApiOperation(value = "위시리스트에 티켓 상품 추가")
	@PostMapping(value = "/wishlist-ticket", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_ticket_add_action(@RequestBody Wishlist wishlist, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// 세션에서 가져온 유저의 아이디로 위시리스트 userId 설정
			User loginUser = (User) session.getAttribute("loginUser");
			String userId = loginUser.getUserId();
			wishlist.setUserId(userId);

			// 이미 담긴 티켓상품이면 위시리스트 중복 추가 불가능
			int tourExist = wishlistService.selectWishlistTicketCount(userId, wishlist.getTicket().getTiNo());
			if (tourExist > 0) {
				code = 3;
				msg = "이미 담긴 상품입니다.";
			} else {
				// 위시리스트에 티켓 상품추가, 성공시 code 1
				wishlistService.insertTicketToWishlist(wishlist);
				// 위시리스트에 투어 상품추가 후 그 위시리스트 데이터에 붙여줌
				wishlist = wishlistService.selectByWishNo(wishlist.getWishNo());
				code = 1;
				msg = "성공";
				data.add(wishlist);
			}
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "위시리스트 티켓추가 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * 투어 상품 위시리스트에 추가
	 */
	@LoginCheck
	@ApiOperation(value = "위시리스트에 투어 상품 추가")
	@PostMapping(value = "/wishlist-tour", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_tour_add_action(@RequestBody Wishlist wishlist, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// 세션에서 가져온 유저의 아이디로 위시리스트 userId 설정
			User loginUser = (User) session.getAttribute("loginUser");
			String userId = loginUser.getUserId();
			wishlist.setUserId(userId);
			// 이미 담긴 투어상품이면 위시리스트 중복 추가 불가능
			int tourExist = wishlistService.selectWishlistTourCount(userId, wishlist.getTour().getToNo());
			if (tourExist > 0) {
				code = 3;
				msg = "이미 담긴 상품입니다.";
			} else {
				// 위시리스트에 투어 상품추가, 성공시 code 1
				wishlistService.insertTourToWishlist(wishlist);
				// 위시리스트에 투어 상품추가 후 그 위시리스트 데이터에 붙여줌
				wishlist = wishlistService.selectByWishNo(wishlist.getWishNo());
				code = 1;
				msg = "성공";
				data.add(wishlist);
			}
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "위시리스트 투어추가 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * 위시리스트 전체 삭제
	 */
	@LoginCheck
	@ApiOperation(value = "위시리스트 전체 삭제")
	@ApiImplicitParam(name = "userId", value = "회원아이디")
	@DeleteMapping(value = "/wishlist/{userId}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_delete_all_action(@PathVariable(value = "userId") String userId) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// userId로 위시리스트 전체 삭제, 성공시 code 1
			int rowCount = wishlistService.deleteWishlist(userId);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패 시 code 2
				code = 2;
				msg = "위시리스트 전체 삭제 실패";
				// 삭제 실패한 리스트 전체 데이터에 붙여줌
				List<Wishlist> failWishlist = wishlistService.selectAllWithTicketAndTour(userId);
				data = failWishlist;
			}
		} catch (Exception e) {
			// 에러 시 code 3
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
	 * 위시리스트 한 개 삭제
	 */
	@LoginCheck
	@ApiOperation(value = "위시리스트 한개 삭제")
	@DeleteMapping(value = "/wishlist/{wishNo}/{userId}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_delete_action(@PathVariable(value = "wishNo") int wishNo,
			@PathVariable(value = "userId") String userId) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// withNo, userId로 위시리스트 1개 삭제, 성공시 code 1
			int rowCount = wishlistService.deleteWishlistByNoAndId(wishNo, userId);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패 시 code 2
				code = 2;
				msg = "위시리스트 삭제 실패";
				// 삭제 실패한 위시리스트 데이터에 붙여줌
				Wishlist failWishlist = wishlistService.selectByWishNo(wishNo);
				data.add(failWishlist);
			}
		} catch (Exception e) {
			// 에러 시 code 3
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
	 * 위시리스트 리스트 보기 + 투어상품만 (user_id)
	 */
	@LoginCheck
	@ApiOperation(value = "위시리스트(투어) 리스트")
	@GetMapping(value = "/wishlist-with-tour", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_list_with_tour(@RequestParam(required = true) String userId) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// userId로 위시리스트(티켓+투어) 리스트 찾기, 성공시 code 1
			List<Wishlist> wishlistList = wishlistService.selectAllWithTicketAndTour(userId);

			for (Wishlist wishlist : wishlistList) {
				if (wishlist.getTour() != null) {
					List<TourImg> tourImgList = tourImgService.findTourImgList(wishlist.getTour().getToNo());
					wishlist.setTourImgList(tourImgList);

					data.add(wishlist);
					code = 1;
					msg = "성공";
				}
			}
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
	/*
	 * 위시리스트 리스트 보기 + 티켓상품만 (user_id)
	 */

	@LoginCheck
	@ApiOperation(value = "위시리스트(티켓) 리스트")
	@GetMapping(value = "/wishlist-with-ticket", produces = "application/json;charset=UTF-8")
	public Map<String, Object> wishlist_list_with_ticket(@RequestParam(required = true) String userId) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Wishlist> data = new ArrayList<>();
		try {
			// userId로 위시리스트(티켓+투어) 리스트 찾기, 성공시 code 1
			List<Wishlist> wishlistList = wishlistService.selectAllWithTicketAndTour(userId);
			for (Wishlist wishlist : wishlistList) {
				if (wishlist.getTicket() != null) {
					List<TicketImg> ticketImgList = ticketImgService
							.selectTicketImgList(wishlist.getTicket().getTiNo());
					wishlist.setTicketImgList(ticketImgList);

					data.add(wishlist);
					code = 1;
					msg = "성공";
				}
			}
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
}
