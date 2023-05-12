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
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;
import com.itwill.my_real_korea.service.tripboard.TripBoardCommentService;
import com.itwill.my_real_korea.service.tripboard.TripBoardService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TripBoardControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	TripBoardService tripBoardService;
	
	@MockBean
	TripBoardCommentService tripBoardCommentService;
	
	@Disabled
	@Test
	void testTripBoardList() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = new PageMakerDto<TripBoard>();
		
		List<TripBoard> tripBoards = new ArrayList<TripBoard>();
		tripBoards.add(new TripBoard(1, "1", "1", null, 0, 0, 1, "1.png", null, null, "1", "1", "1"));
		tripBoards.add(new TripBoard(2, "2", "2", null, 0, 0, 2, "2.png", null, null, "2", "2", "2"));
		
		tripBoardList.setItemList(tripBoards);
		tripBoardList.setPageMaker(new PageMaker(1, 10));
		tripBoardList.setTotRecordCount(2);
		
		given(tripBoardService.selectAllTb(1)).willReturn(tripBoardList);
		
		mockMvc.perform(get("/tripboard-list"))
		.andExpect(status().isOk()) // HTTP 상태코드가 200 OK
		.andExpect(model().attributeExists("tripBoardList")) // model 에 setAttribute 한 값이 있는지
		.andExpect(model().attributeExists("pageNo")) // model 에 setAttribute 한 값이 있는지
		.andExpect(view().name("tripboard-list")) // notice-list 뷰를 반환하는지
		.andDo(print()); // 콘솔에 요청과 응답을 출력
		
		verify(tripBoardService).selectAllTb(1);
		
	}
	
	@Disabled
	@Test
	void testTripBoardDetail() throws Exception {
		TripBoard tripBoard = new TripBoard(1, "1", "1", null, 0, 0, 1, "1.png", null, null, "1", "1", "user1");
		List<TripBoardComment> tripBoardCommentList = new ArrayList<>();
				
		tripBoardCommentList.add(new TripBoardComment(1, "1", null, 1, "user1"));
		tripBoardCommentList.add(new TripBoardComment(2, "2", null, 2, "user1"));
		
		given(tripBoardService.selectByTbNo(1)).willReturn(tripBoard);
		given(tripBoardCommentService.selectAllByTBoNo(1)).willReturn(tripBoardCommentList);
		
		mockMvc.perform(get("/tripboard-detail").param("tBoNo", "1"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("tripBoard"))
		.andExpect(model().attributeExists("tripBoardCommentList"))
		.andExpect(model().attributeExists("tBoNo"))
		.andExpect(view().name("tripboard-detail"))
		.andDo(print());
		
		verify(tripBoardService).selectByTbNo(1);
		verify(tripBoardCommentService).selectAllByTBoNo(1);
	}
	
	@Disabled
	@Test
	void testTripBoardWriteForm() throws Exception {
		mockMvc.perform(get("/tripboard-write-form"))
		.andExpect(status().isOk())
		.andExpect(view().name("tripboard-write-form"))
		.andDo(print());
	}
	
	@Disabled
	@Test
	void testTripBoardModifyForm() throws Exception {
		TripBoard tripBoard = new TripBoard(1, "1", "1", null, 0, 0, 1, "1.png", null, null, "1", "1", "user1");
		given(tripBoardService.selectByTbNo(1)).willReturn(tripBoard);
		
		mockMvc.perform(get("/tripboard-modify-form").param("tBoNo", "1"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("tripBoard"))
		.andExpect(view().name("tripboard-modify-form"))
		.andDo(print());
	}
	
	
}
