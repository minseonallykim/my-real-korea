package com.itwill.my_real_korea.service.tour;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.mapper.CityMapper;
import com.itwill.my_real_korea.mapper.TourImgMapper;
import com.itwill.my_real_korea.mapper.TourMapper;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.util.PageMakerDto;
//@SpringBootTest
@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TourServiceImplTest {
	@Autowired
	private TourService tourService;
	@Autowired
	private CityService cityService;
	@Autowired
	private TourImgService tourImgService;

	void testInsertTour() throws Exception{
		City city=cityService.findByCityNo(2);
		Tour tour=new Tour(0, "잠자기여행", 1, 3, 50, "침대", 50000, "성공해라", "노티스", 0,city);
		int rowCount=tourService.insertTour(tour);
		TourImg tourImg1=new TourImg(0, "침.jpg", tour.getToNo());
		TourImg tourImg2=new TourImg(0, "대.jpg", tour.getToNo());
		TourImg tourImg3=new TourImg(0, "짱.jpg", tour.getToNo());
		tourImgService.insertTourImg(tourImg1);
		tourImgService.insertTourImg(tourImg2);
		tourImgService.insertTourImg(tourImg3);
	}

	void testUpdateTour() throws Exception{
		int rowCount=tourService.updateTour(new Tour(10, "ㄳ", 2, 2, 2, "ㅇㅇ", 3, "ㅇㅇ","ㄴㅇ", 0, new City()));
		assertEquals(rowCount, 1);
	}

	void testDeleteTour() throws Exception{
		int rowCount=tourService.deleteTour(4);
		assertEquals(rowCount, 1);
	}

	void testFindTourByToNo() throws Exception{
		List<TourImg> tourImgList=tourImgService.findTourImgList(3);
		Tour findTour=tourService.findTourWithCityByToNo(3);
		findTour.setTourImgList(tourImgList);
		System.out.println(findTour);
	}

	void testFindTourAll() throws Exception{
		PageMakerDto<Tour> tourList=tourService.findAll(1,"여행",0,0,"toPriceDesc");
		System.out.println(tourList);
	}
/*
	void testFindTourListByKeyword() throws Exception{
		List<Tour> tourList=tourService.findTourListByKeyword("탐험");
		for (Tour tour : tourList) {
			System.out.println(tour);
		}
	}
*/
	void testFindTourListByCityNo() throws Exception{
		PageMakerDto<Tour> tourList=tourService.findAllByFilter(1, 3, "desc");
			System.out.println(tourList);
	}
	
	
/*
	void testFindTourListByToType() throws Exception{
		List<Tour> tourList=tourService.findTourListByToType(1);
		for (Tour tour : tourList) {
			System.out.println(tour);
		}
	}
*/
}
