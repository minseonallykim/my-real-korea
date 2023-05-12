package com.itwill.my_real_korea.dao.tour;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourReserve;
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TourReserveDaoImplTest {
	@Autowired
	private TourReserveDao tourReserveDao;
	
	void testInsertTourReserve() throws Exception{
		int rowCount=tourReserveDao.insertTourReserve(new TourReserve(0, new Date(), 3,"yn", new Tour(10, null, 0, 0, 0, null, 0, null, null, 0, null),"user1"));
		assertEquals(rowCount, 1);
	}

	void testUpdateTourReserve() throws Exception{
		int rowCount=tourReserveDao.updateTourReserve(new TourReserve(13,new Date(),5,"변경",new Tour(10, null, 0, 0, 0, null, 0, null, null, 0, null),"user1")); 
		assertEquals(rowCount, 1);
	}
	void testDeleteTourReserve() throws Exception{
		int rowCount=tourReserveDao.deleteTourReserve(1);
		assertEquals(rowCount, 1);
	}
	void testSelectTourReserveWithTourByToRsNo() throws Exception{
		TourReserve tourReserve=tourReserveDao.findTourReserveWithTourByToRsNo(12);
		System.out.println(tourReserve);
	}
	@Test
	void testSelectAllTourReserveByUserId() throws Exception{
		List<TourReserve> tourReserveList=tourReserveDao.findAllTourReservewithTourByUserId(1, 10, "desc", "user1");
		System.out.println(tourReserveList.size());
		for (TourReserve tourReserve : tourReserveList) {
			System.out.println(tourReserve);
		}
	}
	void testDeleteAllTourReserveByUserId() throws Exception{
		int rowCount=tourReserveDao.deleteAllTourReserveByUserId("user2");
		System.out.println(rowCount);
	}

}
