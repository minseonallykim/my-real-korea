package com.itwill.my_real_korea.service.tripboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.util.PageMakerDto;


//@SpringBootTest
//@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TripBoardServiceImplTest {
	
	@Autowired
	private TripBoardService tripBoardService;
	
	/* 
	 * 게시글 추가
	 */
	//성공
	@Disabled
	@Test
	void testInsertTripBoard() throws Exception {
		int rowCount = tripBoardService.insertTripBoard(new TripBoard(0, "추추제목", "추추내용", new Date(), 1, 1, 3, "추가사진jpg", new Date(), new Date(), "무계획", "인싸만", "user2"));
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 게시글 수정
	 */
	//성공
	@Disabled
	@Test
	void testUpdateTripBoard() throws Exception {
		int rowCount = tripBoardService.insertTripBoard(new TripBoard(3, "제목수정", "내용수정", new Date(), 3, 0, 1, "수정사진1.png", new Date(), new Date(), "무계획", "아무나다좋아", "user1"));
	}
	
	/*
	 * 게시글 삭제
	 */
	//성공
	@Disabled
	@Test
	void testDeleteTripBoard() throws Exception {
		int rowCount = tripBoardService.deleteTripBoard(25);
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 게시글 번호로 게시글 1개 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectByTbNo() throws Exception {
		System.out.println(tripBoardService.selectByTbNo(1));
	}
	
	/*
	 * 게시글 모집상태별로 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectByTbStatusList() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectByTbStatusList(1, 0);
		System.out.println(tripBoardList);
	}

	/*
	 * 게시글 지역별로 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectByCityNoList() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectByCityNoList(1, 4);
		System.out.println(tripBoardList);
	}
	
	/*
	 * 게시글 해시태그별로 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectByHashtagList() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectByHashtagList(1, "인싸만");
		System.out.println(tripBoardList);
	}
	
	/*
	 * 게시판 리스트 정렬(게시글 작성 날짜 기준 내림차순)
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllOrderByDate() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectAllOrderByDate(1);
		System.out.println(tripBoardList);
	}
	

	/*
	 * 게시판 리스트 정렬(조회수 기준 내림차순)
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllOrderByReadCount() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectAllOrderByReadCount(1);
		System.out.println(tripBoardList);
	}
	
	/*
	 * 게시판 리스트
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllTb() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectAllTb(1);
		System.out.println(tripBoardList);
	}
	
	/*
	 * 게시글  총 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllTbCount() throws Exception {
		int rowCount = tripBoardService.selectAllTbCount();
		assertEquals(rowCount, 7);
	}
	
	/*
	 * 모집상태별 게시글 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectStatusCount() throws Exception {
		int rowCount = tripBoardService.selectStatusCount(0);
		assertEquals(rowCount, 3);
	}
	
	/*
	 * 지역별 게시글 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectCityNoCount() throws Exception {
		int rowCount = tripBoardService.selectCityNoCount(4);
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 해시태그별 게시글 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectHashtagCount() throws Exception {
		int rowCount = tripBoardService.selectHashtagCount("인싸만");
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 게시글 조회수 1 증가
	 */
	//성공
	@Disabled
	@Test
	void testIncreaseTbReadCount() throws Exception {
		int rowCount = tripBoardService.increaseTbReadCount(3);
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 키워드로 검색된 동행게시판 리스트
	 */
	//성공
	@Disabled
	@Test
	void testSelectSearchTbList() throws Exception {
		PageMakerDto<TripBoard> tripBoardList = tripBoardService.selectSearchTbList(1, "추추");
		System.out.println(tripBoardList);
	}

	/*
	 * 검색된 게시글 총 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectTbSearchCount() throws Exception {
		int rowCount = tripBoardService.selectTbSearchCount("추추");
		assertEquals(rowCount, 2);
	}
	
}
