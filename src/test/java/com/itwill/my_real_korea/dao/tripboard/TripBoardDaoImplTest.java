package com.itwill.my_real_korea.dao.tripboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;


//@SpringBootApplication
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TripBoardDaoImplTest {
	
	@Autowired
	private TripBoardDao tripBoardDao;
	/* 
	 * 게시글 추가
	 */
	 //성공
	 
	 @Test
	 void testInsertTripBoard() throws Exception { 
		 int rowCount = tripBoardDao.insertTripBoard(new TripBoard(0, "테스트추추", "테스트333", new Date(), 0, 1, 5, "테스트사진1.png", new Date(), new Date(), "무계획", "아무나다좋아", "user1"));
		 assertEquals(rowCount, 1); 
	 }
	 
	/* 
	 * 게시글 수정
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testUpdateTripBoard() throws Exception { 
		 int rowCount = tripBoardDao.updateTripBoard(new TripBoard(26, "제목또수정", "내용또수정", new Date(), 3, 0, 1, "수정사진1.png", new Date(), new Date(), "즉흥적", "맛집투어", "user2"));
		 assertEquals(rowCount, 1); 
	 }
	 
	/* 
	 * 게시글 삭제
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testDeleteTripBoard() throws Exception { 
		 int rowCount = tripBoardDao.deleteTripBoard(17); 
		 assertEquals(rowCount, 1); 
	 }
	 
	/* 
	 * 게시글 번호로 게시글 1개 보기 + City 정보
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectByTbNo() throws Exception {
		 assertNotNull(tripBoardDao.selectByTbNo(1));
		 System.out.println(tripBoardDao.selectByTbNo(1)); 
	 }
	 
	/* 
	 * 게시글 모집상태별로 보기 - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectByTbStatusList() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectByTbStatusList(1, 10, 0);
		 System.out.println(tripBoardList); 
	 }
	 
	/* 
	 * 게시글 지역별로 보기 - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectByCityNoList() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectByCityNoList(1, 10, 1);
		 System.out.println(tripBoardList); 
	 }
	 
	/* 
	 * 게시글 해시태그별로 보기 - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectByHashtagList() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectByHashtagList(1, 10, "인싸만");
		 System.out.println(tripBoardList); 
	 }
	
	/* 
	 * 게시판 리스트 정렬(게시글 작성 날짜 기준 내림차순) - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectAllOrderByDate() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectAllOrderByDate(1, 10);
		 System.out.println(tripBoardList); 
	 }
	 
	/* 
	 * 게시판 리스트 정렬(조회수 기준 내림차순) - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectAllOrderByReadCount() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectAllOrderByReadCount(1, 10);
		 System.out.println(tripBoardList);
	 }
	/* 
	 * 게시판 리스트 + City 정보 - 페이징 처리
	 */
	 @Disabled
	 @Test
	 void testSelectAllTb() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectAllTb(1, 10);
		 System.out.println(tripBoardList);
	 }
	/* 
	 * 게시글 총 개수
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectAllTbCount() throws Exception { 
		 int rowCount = tripBoardDao.selectAllTbCount(); 
		 assertEquals(rowCount, 7); 
	 }
	
	/* 
	 * 모집상태별 게시글 개수
	 */
	//성공
	@Disabled
	@Test 
	void testSelectStatusCount() throws Exception { 
		int rowCount = tripBoardDao.selectStatusCount(0); 
		assertEquals(rowCount, 3); 
	}
	 
	/* 
	 * 지역별 게시글 개수
	 */
	 //성공
	 @Disabled
	 @Test void testSelectCityNoCount() throws Exception { 
		 int rowCount = tripBoardDao.selectCityNoCount(4); 
		 assertEquals(rowCount, 1); 
	 }
	 
	/* 
	 * 해시태그별 게시글 개수
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectHashtagCount() throws Exception { 
		 int rowCount = tripBoardDao.selectHashtagCount("인싸만"); 
		 assertEquals(rowCount, 1); 
	 }
	
	/* 
	 * 게시글 조회수 1 증가
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testIncreaseTbReadCount() throws Exception { 
		 int rowCount = tripBoardDao.increaseTbReadCount(3); 
		 assertEquals(rowCount, 1); 
	 }
	
	/* 
	 * 키워드로 검색된 동행게시판 리스트 - 페이징 처리
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectSearchTbList() throws Exception { 
		 List<TripBoard> tripBoardList = tripBoardDao.selectSearchTbList(1, 10, "추추");
		 System.out.println(tripBoardList); 
	 }
	 
	/* 
	 * 검색된 게시글 총 개수
	 */
	 //성공
	 @Disabled
	 @Test 
	 void testSelectTbSearchCount() throws Exception { 
		 int rowCount =tripBoardDao.selectTbSearchCount("추추"); 
		 assertEquals(rowCount, 2); 
	}
	
}
