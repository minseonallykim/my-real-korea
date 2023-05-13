package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.tour.TourImgService;
import com.itwill.my_real_korea.service.wishlist.WishlistService;

@Controller
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;
	@Autowired
	private TourImgService tourImgService;
	@Autowired
	private TicketImgService ticketImgService;
	@Autowired
	private CityService cityService;

	// 위시리스트 리스트 + 티켓상품 + 투어상품 전체 보기 (위시리스트 첫화면)
	@LoginCheck
	@GetMapping(value = "/wishlist")
	public String wishlist(HttpServletRequest request, HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		List<Wishlist> wishlistList = wishlistService.selectAllWithTicketAndTour(loginUser.getUserId());
		List<Wishlist> tourWishList = new ArrayList<Wishlist>();
		List<Wishlist> ticketWishList = new ArrayList<Wishlist>();
		for (Wishlist wishlist : wishlistList) {
			if (wishlist.getTour() != null) {
				List<TourImg> tourImgList = tourImgService.findTourImgList(wishlist.getTour().getToNo());
				wishlist.setTourImgList(tourImgList);
				// 투어가 존재하는 위시리스트들만 모음
				tourWishList.add(wishlist);
			} else if (wishlist.getTicket() != null) {
				List<TicketImg> ticketImgList = ticketImgService.selectTicketImgList(wishlist.getTicket().getTiNo());
				wishlist.setTicketImgList(ticketImgList);
				// 티켓이 존재하는 위시리스트들만 모음
				ticketWishList.add(wishlist);
			}
		}
		try {
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("wishlistList", wishlistList);
		model.addAttribute("tourWishList", tourWishList);
		model.addAttribute("ticketWishList", ticketWishList);
		model.addAttribute("loginUser", loginUser);

		return "wishlist";
	}

}
