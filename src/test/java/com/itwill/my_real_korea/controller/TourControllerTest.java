package com.itwill.my_real_korea.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.tour.TourReview;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.tour.TourImgService;
import com.itwill.my_real_korea.service.tour.TourReviewService;
import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TourControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	TourService tourService;
	@MockBean
	TourImgService tourImgService;
	@MockBean
	TourReviewService tourReviewService; 
	
	void testTourList() throws Exception{
		PageMakerDto<Tour> tours=new PageMakerDto<>();
		List<Tour> tourList=new ArrayList<>();
		
		tourList.add(new Tour(1, null, 0, 0, 0, null, 0, null, null, 0, null));
		tourList.add(new Tour(2, null, 0, 0, 0, null, 0, null, null, 0, null));

		tours.setItemList(tourList);
		tours.setPageMaker(new PageMaker(1, 10));
		tours.setTotRecordCount(2);
		
		
		
		given(tourService.findAll(1, null, 0, 0, null)).willReturn(tours);
		
		mockMvc.perform(get("/tour-list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("tour-list"))
			   .andDo(print());
		
		verify(tourService).findAll(1, null, 0, 0, null);
	}
	
	@Test
	void testTourDetail() throws Exception{
		Tour tour=new Tour(1, null, 0, 0, 0, null, 0, null, null, 0, null);
		TourImg tourImg1=new TourImg(1, null, 1);
		TourImg tourImg2=new TourImg(2, null, 1);
		TourImg tourImg3=new TourImg(3, null, 1);
		TourReview tourReview1=new TourReview(1, null, "집", "보내주세요", null, 5, 1);
		TourReview tourReview2=new TourReview(2, null, "가", "보내주세요", null, 5, 1);
		TourReview tourReview3=new TourReview(3, null, "자", "보내주세요", null, 5, 1);
		
		List<TourImg> tourImgList=tourImgService.findTourImgList(1);
		tourImgList.add(tourImg1);
		tourImgList.add(tourImg2);
		tourImgList.add(tourImg3);
		List<TourReview> tourReviewList=tourReviewService.findByToNo(1);
		tourReviewList.add(tourReview1);
		tourReviewList.add(tourReview2);
		tourReviewList.add(tourReview3);
		System.out.println(tour);	
		
		tour.setTourImgList(tourImgList);
		System.out.println(tour);		
		
		
		given(tourService.findTourWithCityByToNo(1)).willReturn(tour);
	    given(tourImgService.findTourImgList(1)).willReturn(tourImgList);
	    given(tourReviewService.findByToNo(1)).willReturn(tourReviewList);

	    // when and then
	    mockMvc.perform(get("/tour-detail").param("toNo", "1"))
	            .andExpect(status().isOk())
	            .andExpect(model().attributeExists("tour"))
	            .andExpect(model().attributeExists("tourReviewList"))
	            .andExpect(view().name("tour-detail"))
	            .andDo(print());

	    verify(tourService).findTourWithCityByToNo(1);
	}
	
	@Test
	void testTourDe() throws Exception{
		
	}

}
