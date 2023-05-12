package com.itwill.my_real_korea.dao.tour;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.tour.TourReview;
import com.itwill.my_real_korea.dto.user.User;
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TourReviewDaoImplTest {

	@Autowired
	private TourReviewDao tourReviewDao;
	
	void testInsertTourReview() throws Exception{
		int rowCount=tourReviewDao.insertTourReview(new TourReview(0, null, "테스트!!!", "리뷰만", "default.jpg", 3, 5));
		assertEquals(rowCount, 1);
	}

	@Test
	void testSelectByToNo() throws Exception{
		List<TourReview> tourReviewList=tourReviewDao.selectByToNo(1);
		System.out.println(tourReviewList.size());
		for (TourReview tourReview : tourReviewList) {
			System.out.println(tourReview);
		}
	}

	//왜 안될까 
	void testUpdateTourReview() throws Exception{
		int rowCount=tourReviewDao.updateTourReview(new TourReview(4,null,"변경","두시엔 잔다","default.jpg",5,4));
		assertEquals(rowCount, 1);
	}

	void testDeleteTourReview() throws Exception{
		int rowCount=tourReviewDao.deleteTourReview(1);
		assertEquals(rowCount, 1);
	}

	void testSelectByUserId() throws Exception{
		List<TourReview> tourReviewList=tourReviewDao.selectByUserId("user2");
		System.out.println(tourReviewList.size());
		for (TourReview tourReview : tourReviewList) {
			System.out.println(tourReview);
		}
	}

}
