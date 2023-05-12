package com.itwill.my_real_korea.service.tripboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;


//@SpringBootTest
//@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TripBoardCommentServiceImplTest {
	
	@Autowired
	private TripBoardCommentService tripBoardCommentService;
	
	/*
	 * 댓글 추가
	 */
	//성공
	@Disabled
	@Test
	void testInsertTripBoardComment() throws Exception {
		int rowCount = this.tripBoardCommentService.insertTripBoardComment(new TripBoardComment(0, "추가추가", new Date(), 1, "user3" ));
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 댓글 수정
	 */
	//성공
	@Disabled
	@Test
	void testUpdateTripBoardComment() throws Exception {
		TripBoardComment tripBoardComment = new TripBoardComment(9, "service수정", new Date(), 0, "user3");
		assertEquals(tripBoardCommentService.updateTripBoardComment(tripBoardComment), 1);
	}
	
	/*
	 * 댓글 삭제
	 */
	//성공
	@Disabled
	@Test
	void testDeleteTripBoardComment() throws Exception {
		int rowCount = tripBoardCommentService.deleteTripBoardComment(9);
		assertEquals(rowCount, 1);
	}
	
	/*
	 * 댓글 1개 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectByNo() throws Exception {
		TripBoardComment tripBoardComment = tripBoardCommentService.selectByNo(1);
		System.out.println(tripBoardComment);
	}
	
	/*
	 * N번 게시글의 댓글 전체 보기
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllByTBoNo() throws Exception {
		List<TripBoardComment> tripBoardCommentList = tripBoardCommentService.selectAllByTBoNo(1);
		System.out.println(tripBoardCommentList);
	}
	
	/*
	 * 댓글 총 개수
	 */
	//성공
	@Disabled
	@Test
	void testSelectAllCount() throws Exception {
		System.out.println(tripBoardCommentService.selectAllCount());
	}
	

}
