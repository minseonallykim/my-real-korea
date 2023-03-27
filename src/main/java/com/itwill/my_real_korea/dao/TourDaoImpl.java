package com.itwill.my_real_korea.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.my_real_korea.dto.Tour;
import com.itwill.my_real_korea.mapper.TourMapper;

@Repository
public class TourDaoImpl implements TourDao{
	@Autowired
	private TourMapper tourMapper;
	
	public TourDaoImpl() {
		System.out.println("TourDaoImp 기본생성자 호출");
	}
	
	@Override
	public int insertTour(Tour tour) throws Exception {
		// 투어상품 추가
		return tourMapper.insertTour(tour);
	}

	@Override
	public int updateTour(Tour tour) throws Exception {
		// 투어상품 수정
		return tourMapper.updateTour(tour);
	}

	@Override
	public List<Tour> selectAll() throws Exception {
		// 투어상품 리스트 전체 출
		return tourMapper.selectAll();
	}

	@Override
	public Tour selectByToNo(int toNo) throws Exception {
		// 투어상품 상품번호로 상세보기
		return tourMapper.selectByToNo(toNo);
	}

	@Override
	public List<Tour> selectByKeyword(String keyword) throws Exception {
		// 키워드로 투어상품 검색
		return tourMapper.selectByKeyword(keyword);
	}

	@Override
	public List<Tour> selectByCityNo(int cityNo) throws Exception {
		// 지역으로 투어상품 검색 
		return tourMapper.selectByCityNo(cityNo);
	}

	@Override
	public List<Tour> selectByToType(int toType) throws Exception {
		// 여행타입으로 투어상품 검색
		return tourMapper.selectByToType(toType);
	}

	@Override
	public int deleteTour(int toNo) throws Exception {
		// 투어상품 삭제
		return tourMapper.deleteTour(toNo);
	}
}