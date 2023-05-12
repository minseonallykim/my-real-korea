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

import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.service.notice.NoticeService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class NoticeControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	NoticeService noticeService;

	@Disabled
	@Test
	void testNotice_list() throws Exception{
		// 임의로 noticeList 만들기
		PageMakerDto<Notice> noticeList = new PageMakerDto<Notice>();
		
		List<Notice> notices = new ArrayList<Notice>();
		notices.add(new Notice(1, null, null, null, 0, null, null));
		notices.add(new Notice(2, null, null, null, 0, null, null));
		notices.add(new Notice(3, null, null, null, 0, null, null));
		// noticeList에 notices 붙이기
		noticeList.setItemList(notices);
		noticeList.setPageMaker(new PageMaker(1, 10));
		noticeList.setTotRecordCount(3);
		// selectAll() 메소드 실행시 noticeList 가 return 된다고 가정
		given(noticeService.selectAll(1)).willReturn(noticeList);
	
		mockMvc.perform(get("/notice-list")) // get 방식으로 요청
		.andExpect(status().isOk()) // HTTP 상태코드가 200 OK
		.andExpect(model().attributeExists("noticeList")) // model 에 setAttribute 한 값이 있는지
		.andExpect(model().attributeExists("pageNo")) // model 에 setAttribute 한 값이 있는지
		.andExpect(view().name("notice-list")) // notice-list 뷰를 반환하는지
		.andDo(print()); // 콘솔에 요청과 응답을 출력
		
		verify(noticeService).selectAll(1); // 메소드 호출되는지 검증
	}
	
	
	@Test
	void testNotice_detail() throws Exception{
		// 임의로 notice 만들기
		Notice notice = new Notice(1, null, null, null, 0, null, null);
		// selectByNo(1) 메소드 실행시 notice 가 return 된다고 가정
		given(noticeService.selectByNo(1)).willReturn(notice);
	
		mockMvc.perform(get("/notice-detail").param("nNo", "1")) 
		.andExpect(status().isOk()) 
		.andExpect(model().attributeExists("notice")) 
		.andExpect(model().attributeExists("nNo")) 
		.andExpect(view().name("notice-detail")) 
		.andDo(print()); 
		
		verify(noticeService).selectByNo(1); 
	}

	@Disabled
	@Test
	void testNotice_write_form() throws Exception {
		mockMvc.perform(get("/notice-write-form"))
		.andExpect(status().isOk())
		.andExpect(view().name("notice-write-form"))
		.andDo(print());
	}
	
	@Disabled
	@Test
	void testNotice_modify_form() throws Exception {
		mockMvc.perform(get("/notice-modify-form"))
		.andExpect(status().isOk())
		.andExpect(view().name("notice-modify-form"))
		.andDo(print());
	}

}