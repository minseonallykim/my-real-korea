package com.itwill.my_real_korea.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.service.wishlist.WishlistService;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class WishlistControllerTest {

	protected MockHttpSession session;
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	WishlistService wishlistService;
	
	@Test
	void testWishlist() throws Exception {
		// 임의로 session 만들기
		session = new MockHttpSession();
		String sUserId = "KIM";
		session.setAttribute("sUserId", sUserId);
		
		// 임의로 wishlistList 만들기
		List<Wishlist> wishlistList = new ArrayList<>();
		wishlistList.add(new Wishlist(1, sUserId, new Ticket(1, sUserId, null, 0, sUserId, sUserId, 0, null), null));
		wishlistList.add(new Wishlist(2, sUserId, null, new Tour(2, sUserId, 0, 0, 0, sUserId, 0, sUserId, sUserId, 0, null)));
		wishlistList.add(new Wishlist(3, sUserId, new Ticket(3, sUserId, null, 0, sUserId, sUserId, 0, null), null));
		for (Wishlist wishlist : wishlistList) {
			List<TourImg> tourImgList = Arrays.asList(new TourImg(0, "dd", 2), new TourImg(0, "ff", 2));
			List<TicketImg> ticketImgList = Arrays.asList(new TicketImg(0, "gg", 1), new TicketImg(0, "ggg", 1), 
														new TicketImg(0, "ee", 3), new TicketImg(0, "vv", 3));
			wishlist.setTourImgList(tourImgList);
			wishlist.setTicketImgList(ticketImgList);
		}
		
		// selectAllWithTicketAndTour() 메소드 실행시 wishlistList 가 return 된다고 가정
		given(wishlistService.selectAllWithTicketAndTour(sUserId)).willReturn(wishlistList);
		mockMvc.perform(get("/wishlist").session(session)) // get 방식으로 요청
		.andExpect(status().isOk()) // HTTP 상태코드가 200 OK
		.andExpect(model().attributeExists("wishlistList")) // model 에 setAttribute 한 값이 있는지
		.andExpect(model().attributeExists("sUserId")) // model 에 setAttribute 한 값이 있는지
		.andExpect(model().attributeExists("tourImgList")) // model 에 setAttribute 한 값이 있는지
		.andExpect(model().attributeExists("ticketImgList")) // model 에 setAttribute 한 값이 있는지
		.andExpect(view().name("wishlist")) // wishlist 뷰를 반환하는지
		.andDo(print());  // 콘솔에 요청과 응답을 출력
		
		verify(wishlistService).selectAllWithTicketAndTour(sUserId); // 메소드 호출되는지 검증
		
	}

}
