package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.tour.TourReserve;
import com.itwill.my_real_korea.dto.tour.TourReview;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.tour.TourImgService;
import com.itwill.my_real_korea.service.tour.TourReserveService;
import com.itwill.my_real_korea.service.tour.TourReviewService;
import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.util.FileUploadNotFoundException;
import com.itwill.my_real_korea.util.FileUploadService;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class TourRestController {
	private TourService tourService;
	private TourImgService tourImgService;
	private CityService cityService;
	private TourReserveService tourReserveService;
	private TourReviewService tourReviewService;
	private FileUploadService fileUploadService;

	public TourRestController(TourService tourService, TourImgService tourImgService, CityService cityService,
			TourReserveService tourReserveService, TourReviewService tourReviewService,
			FileUploadService fileUploadService) {
		this.tourService = tourService;
		this.tourImgService = tourImgService;
		this.cityService = cityService;
		this.tourReserveService = tourReserveService;
		this.tourReviewService = tourReviewService;
		this.fileUploadService = fileUploadService;
	}

	/****** HTML <img>에 이미지 출력 ******/
	@ResponseBody // 이 메소드는 HTTP응답의 body로 사용될 객체를 반환한다.
	@GetMapping(value = "/tourReviewImage/{filename}", produces = { MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE })
	public Resource showImage(@PathVariable String filename) throws Exception {
		// urlResource로 이미지 파일을 읽어서 @ResponseBody로 이미지 바이너리 반환
		return new UrlResource("file:" + fileUploadService.getFullPath(filename));
	}

	// 1. 투어상품 전체 리스트 보기
	@ApiOperation(value = "투어상품리스트(필터&정렬)")
	@PostMapping(value = "/tour-list-ajax", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tourListAjax(@RequestBody Map<String, String> map, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<Tour> data = null;
		int pageNo = Integer.parseInt(map.get("pageNo"));
		int cityNo = Integer.parseInt(map.get("cityNo"));
		int toType = Integer.parseInt(map.get("toType"));
		String keyword = map.get("keyword");
		String sortOrder = map.get("sortOrder");
		User loginUser = (User) session.getAttribute("loginUser");
		try {
			PageMakerDto<Tour> tourListPage = tourService.findAll(pageNo, keyword, cityNo, toType, sortOrder);
			List<Tour> tourList = new ArrayList<>();
			for (Tour tour : tourListPage.getItemList()) {
				if (tourReviewService.findByToNo(tour.getToNo()).size() == 0) {
					// 후기가 없을 때
					tour.setToScore(0);
					tourList.add(tour);
				} else {
					// 후기가 있을 때
					int tourScore = tourReviewService.calculateTourScore(tour.getToNo());
					tour.setToScore(tourScore);
					tourList.add(tour); // tourList에 후기 평점 평균 붙이기
				}
			}
			tourListPage.setItemList(tourList);
			data = tourListPage;
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		resultMap.put("loginUser", loginUser);
		return resultMap;
	}

	// 2. 투어 리뷰 남기기
	@LoginCheck
	@ApiOperation(value = "투어상세보기(투어리뷰남기기)")
	@PostMapping(value = "/tour-review-action", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tourReviewAction(@ModelAttribute TourReview tourReview,
			@RequestParam(name = "file", required = false) MultipartFile file, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		int toScore = 0;
		int toCount = 0;
		int toReviewCount = 0;
		String msg = "성공";
		List<TourReview> data = new ArrayList<>();
		User loginUser = (User) session.getAttribute("loginUser");
		try {
			tourReview.setToReviewImg("");
			tourReview.setUsers(loginUser);
			tourReviewService.insertTourReview(tourReview);
			if (file != null) {
				// 업로드 파일 존재하면
				fileUploadService.store(file); // 파일 업로드 한다면 파일 저장
				tourReviewService.updateToReviewUpload(file.getOriginalFilename(), tourReview.getToReviewNo());
			} else if (tourReview.getToReviewImg() != null) {
				// 기존 파일 사용한다면
				tourReviewService.updateToReviewImg(tourReview.getToReviewImg(), tourReview.getToReviewNo());
			}
			List<TourReview> tourReviewList = tourReviewService.findByToNo(tourReview.getToNo());
			data = tourReviewList;

			// toScore 붙여서 리뷰쪽에 평균 찍기 위함
			int totScore = 0;
			for (TourReview tempTourReview : tourReviewList) {
				totScore += tempTourReview.getToReviewStar();
			}
			toScore = totScore / tourReviewList.size();

			// toCount 붙여서 리뷰쪽에 구매 갯수 찍기 위함
			toCount = tourService.findTourWithCityByToNo(tourReview.getToNo()).getToCount();
			System.out.println(data);

			// toReviewCount 붙여서 리뷰쪽에 리뷰 갯수 붙이기 위함
			toReviewCount = tourReviewList.size();
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		resultMap.put("toScore", toScore);
		resultMap.put("toCount", toCount);
		resultMap.put("toReviewCount", toReviewCount);
		resultMap.put("loginUser", loginUser);
		return resultMap;
	}

	// 3. 투어리뷰 수정하기
	@LoginCheck
	@ApiOperation(value = "투어리뷰 수정하기")
	@PutMapping(value = "/tour-review-update", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tourReviewUpdate(@ModelAttribute TourReview tourReview,
			@RequestParam(name = "file", required = false) MultipartFile file, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		int toScore = 0;
		int toCount = 0;
		int toReviewCount = 0;
		String msg = "성공";
		List<TourReview> data = new ArrayList<>();
		User loginUser = (User) session.getAttribute("loginUser");
		try {
			tourReview.setToReviewImg("");
			tourReviewService.updateTourReview(tourReview);
			if (file != null) {
				// 업로드 파일 존재하면
				fileUploadService.store(file); // 파일 업로드 한다면 파일 저장
				tourReviewService.updateToReviewUpload(file.getOriginalFilename(), tourReview.getToReviewNo());
			} else if (tourReview.getToReviewImg() != null) {
				// 기존 파일 사용한다면
				tourReviewService.updateToReviewImg(tourReview.getToReviewImg(), tourReview.getToReviewNo());
			}
			List<TourReview> tourReviewList = tourReviewService.findByToNo(tourReview.getToNo());
			data = tourReviewList;

			// toScore 붙여서 리뷰쪽에 평균 찍기 위함
			int totScore = 0;
			for (TourReview tempTourReview : tourReviewList) {
				totScore += tempTourReview.getToReviewStar();
			}
			toScore = totScore / tourReviewList.size();

			// toCount 붙여서 리뷰쪽에 구매 갯수 찍기 위함
			toCount = tourService.findTourWithCityByToNo(tourReview.getToNo()).getToCount();

			// toReviewCount 붙여서 리뷰쪽에 리뷰 갯수 붙이기 위함
			toReviewCount = tourReviewList.size();
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		resultMap.put("toScore", toScore);
		resultMap.put("toCount", toCount);
		resultMap.put("toReviewCount", toReviewCount);
		resultMap.put("loginUser", loginUser);
		return resultMap;

	}

	// 4. 투어리뷰 삭제하기
	@LoginCheck
	@ApiOperation(value = "투어리뷰 삭제하기")
	@DeleteMapping(value = "/tour-review-delete/{toNo}/{toReviewNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tourReviewDelete(@PathVariable int toReviewNo, @PathVariable int toNo,
			HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		int toScore = 0;
		int toCount = 0;
		int toReviewCount = 0;
		String msg = "성공";
		List<TourReview> data = new ArrayList<>();
		User loginUser = (User) session.getAttribute("loginUser");
		try {
			tourReviewService.deleteTourReview(toReviewNo);
			List<TourReview> tourReviewList = tourReviewService.findByToNo(toNo);
			data = tourReviewList;

			// toScore 붙여서 리뷰쪽에 평균 찍기 위함
			int totScore = 0;
			for (TourReview tempTourReview : tourReviewList) {
				totScore += tempTourReview.getToReviewStar();
			}
			toScore = totScore / tourReviewList.size();

			// toCount 붙여서 리뷰쪽에 구매 갯수 찍기 위함
			toCount = tourService.findTourWithCityByToNo(toNo).getToCount();

			// toReviewCount 붙여서 리뷰쪽에 리뷰 갯수 붙이기 위함
			toReviewCount = tourReviewList.size();
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		resultMap.put("toScore", toScore);
		resultMap.put("toCount", toCount);
		resultMap.put("toReviewCount", toReviewCount);
		resultMap.put("toNo", toNo);
		resultMap.put("loginUser", loginUser);
		return resultMap;
	}

	@ExceptionHandler(FileUploadNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(FileUploadNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}