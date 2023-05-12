package com.itwill.my_real_korea.dao.tripboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TripBoardCommentDaoImplTest {
	@Autowired
	private TripBoardCommentDao tripBoardCommentDao;
	
	//성공
	@Disabled
	@Test
	void testInsertTripBoardComment() throws Exception {
		TripBoardComment tripBoardComment = new TripBoardComment(0,"추가", new Date(), 1, "user2");
		assertEquals(tripBoardCommentDao.insertTripBoardComment(tripBoardComment), 1);
	}
	
	//성공
	@Disabled
	@Test
	void testUpdateTripBoardComment() throws Exception {
		TripBoardComment tripBoardComment = new TripBoardComment(8,"또수정", new Date(), 0, "user1");
		assertEquals(tripBoardCommentDao.updateTripBoardComment(tripBoardComment), 1);
	}
	
	//성공
	@Disabled
	@Test
	void testDeleteTripBoardComment() throws Exception {
		int rowCount = tripBoardCommentDao.deleteTripBoardComment(3);
		assertEquals(rowCount, 1);
	}
	

	//성공
	@Disabled
	@Test
	void testSelectByNo() throws Exception {
		TripBoardComment tripBoardComment = tripBoardCommentDao.selectByNo(1);
		System.out.println(tripBoardComment);
	}
	
	//성공
	@Disabled
	@Test
	void testSelectAllByTBoNo() throws Exception {
		List<TripBoardComment> tripBoardCommentList = tripBoardCommentDao.selectAllByTBoNo(1);
		System.out.println(tripBoardCommentList);
	}
	
	//성공
	@Disabled
	@Test
	void testSelectAllCount() throws Exception {
		System.out.println(tripBoardCommentDao.selectAllCount());
	}

}
