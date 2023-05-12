package com.itwill.my_real_korea.dao.tour;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.tour.TourImg;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TourImgDaoImplTest{
	
	@Autowired
	private TourImgDao tourImgDao;

	void testInsertTourImg() {
		int rowCount=tourImgDao.insertTourImg(new TourImg(0, "default.jpg", 5));
		assertEquals(rowCount,1);
	}

	void testDeleteTourImg() throws Exception{
		int rowCount=tourImgDao.deleteTourImg(5);
		assertEquals(rowCount, 1);
	}

	void testSelectTourImgList() throws Exception{
		List<TourImg> tourImgList=tourImgDao.findTourImgList(3);
		System.out.println(tourImgList.size());
		for (TourImg tourImg : tourImgList) {
			System.out.println(tourImg.toString());
		}
	}

	@Test
	void testDeleteTourAllImg() {
		int rowCount=tourImgDao.deleteTourAllImg(3);
		System.out.println(rowCount);
	}

}
