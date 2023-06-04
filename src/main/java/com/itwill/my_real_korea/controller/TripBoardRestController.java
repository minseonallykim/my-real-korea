package com.itwill.my_real_korea.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.tripboard.TripBoardService;
import com.itwill.my_real_korea.util.FileUploadNotFoundException;
import com.itwill.my_real_korea.util.FileUploadService;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class TripBoardRestController {

	private final FileUploadService storageService;

	@Autowired
	private TripBoardService tripBoardService;
	@Autowired
	private CityService cityService;

	// TripBoardRestController 객체 생성 시 storaeService 사용, 파일 업로드 기능 구현가능하도록
	@Autowired
	public TripBoardRestController(FileUploadService storageService) {
		this.storageService = storageService;
	}

	// HTML <img>에 이미지 출력
	@ResponseBody // 이 메소드는 HTTP응답의 body로 사용될 객체를 반환한다.
	@GetMapping(value = "/tripboardimages/{filename}", produces = { MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE })
	public Resource showImage(@PathVariable String filename) throws MalformedURLException {
		// 파일 경로에서 파일 이름 추출
		String fname = new File(filename).getName();
		// UrlResource로 이미지 파일을 읽어서 @ResponseBody로 이미지 바이너리 반환
		return new UrlResource("file:" + storageService.getFullPath(fname));
	}

	/*
	 * 동행 게시글 추가
	 */
	@LoginCheck
	@ApiOperation(value = "동행게시글 추가")
	@PostMapping(value = "/tripboard", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripboard_write_action(@ModelAttribute TripBoard tripBoard, @RequestParam int cityNo,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoard> data = new ArrayList<TripBoard>();

		try {
			tripBoard.setTBoImg("gyeonggi.jpg");
			// 동행 게시글 쓰기, 성공시 code 1
			City city = cityService.findByCityNo(cityNo);
			tripBoard.setCity(city);
			tripBoardService.insertTripBoard(tripBoard);

			// 파일업로드
			if (file != null) {
				// 선택된 파일이 있다면, 파일 저장
				storageService.store(file);
				// 업로드된 파일로 tUploadFile update
				tripBoardService.updateUploadFile(file.getOriginalFilename(), tripBoard.getTBoNo());
				System.out.println(">>>>>>>>>>>>" + file.getOriginalFilename());
				tripBoard.setTBoImg("");
			}
			// 이미지 있다면 tBoImg update
			if (tripBoard.getTBoImg() != null) {
				tripBoardService.updateTripBoardImg(tripBoard.getTBoImg(), tripBoard.getTBoNo());
			}
			// 동행 게시글 쓰기 후 데이터 자동 붙여줌
			tripBoard = tripBoardService.selectByTbNo(tripBoard.getTBoNo());
			code = 1;
			msg = "성공";
			data.add(tripBoard);
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "게시글 쓰기 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * 동행 게시글 수정
	 */
	@LoginCheck
	@ApiOperation(value = "동행게시글 수정")
	@ApiImplicitParam(name = "tBoNo", value = "동행게시글 번호") /* Api 의 파라미터(하나)만 가지고 오려고 사용 */
	@PutMapping(value = "/tripboard/{tBoNo}", produces = "application/json;charset=UTF-8") /* text */
	public Map<String, Object> tripboard_modify_action(@PathVariable(value = "tBoNo") int tBoNo,
			@ModelAttribute TripBoard tripBoard, @RequestParam int cityNo,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoard> data = new ArrayList<TripBoard>();
		try {
			// String getTBoImg = tripBoard.getTBoImg();

			// tBoNo로 동행 게시글 1개 찾기, 수정 성공시 code 1
			City city = cityService.findByCityNo(cityNo);
			tripBoard.setCity(city);

			TripBoard findTripBoard = tripBoardService.selectByTbNo(tBoNo);
			if (findTripBoard != null) {
				tripBoard.setTBoNo(tBoNo);
				// tripBoard.setTBoImg(getTBoImg);
				// 파일업로드
				if (file != null) {
					// 선택된 파일이 있다며, 파일 저장
					storageService.store(file);
					// 업로드 된 파일로 tUploadFile update, 기존의 tBoImg null만들기
					tripBoardService.updateTripBoardImgNull(tBoNo);
					tripBoardService.updateUploadFile(file.getOriginalFilename(), tBoNo);
					System.out.println(">>>>>>>>>>>>" + file.getOriginalFilename());
				}
				tripBoardService.updateTripBoard(tripBoard);
				System.out.println(">>>>>>>>>>>>>>>>>>>" + tripBoard.getTBoImg());
				code = 1;
				msg = "성공";
				// 수정 성공한 동행 게시판 객체 데이터 붙임
				data.add(tripBoard);
			} else {
				// 실패시 code 2
				code = 2;
				msg = "게시글 수정 실패";
			}

		} catch (Exception e) {
			// 에러 발생 시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * 동행 게시글 삭제
	 */
	@LoginCheck
	@ApiOperation(value = "동행 게시글 삭제") /* swagger 에서 출력 값 */
	@ApiImplicitParam(name = "tBoNo", value = "동행 게시글 번호") /* swagger 에서 출력 값 */
	@DeleteMapping(value = "/tripboard/{tBoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripboard_delete_action(@PathVariable(value = "tBoNo") int tBoNo,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoard> data = new ArrayList<TripBoard>();
		try {
			int rowCount = tripBoardService.deleteTripBoard(tBoNo);
			if (rowCount == 1) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "게시글 삭제 실패";
				TripBoard failTripBoard = tripBoardService.selectByTbNo(tBoNo);
				data.add(failTripBoard);
			}
		} catch (Exception e) {
			// 에러시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의바랍니다.";
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	/*
	 * 키워드로 검색된 동행게시판 리스트(keyword) - 페이징 처리
	 */
	@ApiOperation(value = "동행게시판 title 검색 결과 리스트")
	@GetMapping(value = "/tripboard-search/{keyword}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_search_list(@RequestParam(required = false, defaultValue = "1") int pageNo,
			@PathVariable String keyword) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)와 검색 keyword로 동행 게시글 키워드로 찾기. 성공시 code 1
			tripBoardList = tripBoardService.selectSearchTbList(pageNo, keyword);
			if (tripBoardList.getTotRecordCount() != 0 && tripBoardList != null) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "해당 키워드와 일치하는 게시글이 없습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;
	}

	/*
	 * 동행 게시글 모집 상태별로 보기(tBoStatus) - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시글 모집 상태별로 보기")
	@GetMapping(value = "/tripboard-status-list", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_status_list(@RequestParam(required = false, defaultValue = "1") int tBoNo,
			@RequestParam(required = true) int tBoStatus) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)와 검색 status로 동행 게시글 키워드로 찾기. 성공시 code 1
			tripBoardList = tripBoardService.selectByTbStatusList(tBoNo, tBoStatus);
			if (tripBoardList.getTotRecordCount() != 0 && tripBoardList != null) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "일치하는 게시물이 없습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;
	}

	/*
	 * 동행 게시글 해시태그별로 보기(hashtag) - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시글 해시태그별로 보기(hashtag)")
	@GetMapping(value = "/tripboard-hashtag-list", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_hashtag_list(@RequestParam(required = false, defaultValue = "1") int tBoNo,
			@RequestParam(required = true) String hashtag) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)와 검색 hashtag로 동행 게시글 키워드로 찾기. 성공시 code 1
			tripBoardList = tripBoardService.selectByHashtagList(tBoNo, hashtag);
			if (tripBoardList.getTotRecordCount() != 0 && tripBoardList != null) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "해당 키워드 결과 값이 없습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;
	}

	/*
	 * 동행 게시글 지역별로 보기(cityNo) - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시글 지역별로 보기(cityNo)")
	@GetMapping(value = "/tripboard-city-list", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_hashtag_list(@RequestParam(required = false, defaultValue = "1") int tBoNo,
			@RequestParam(required = true) int cityNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)와 검색 cityNo로 동행 게시글 키워드로 찾기. 성공시 code 1
			tripBoardList = tripBoardService.selectByCityNoList(tBoNo, cityNo);
			if (tripBoardList.getTotRecordCount() != 0 && tripBoardList != null) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "해당 지역과 일치하는 게시글이 없습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;
	}

	/*
	 * 동행 게시판 리스트 정렬(조회수 기준 내림차순(readcount)) - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시판 조회수 정렬 리스트(readcount)")
	@GetMapping(value = "/tripboard-readcount", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripboard_readcount_list(
			@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)로 동행 게시글 조회순 찾기, 성공시 code 1
			tripBoardList = tripBoardService.selectAllOrderByReadCount(pageNo);
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			// 에러 발생시 code 2
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;

	}

	/*
	 * 동행 게시판 리스트 정렬(게시글 작성 날짜 기준 내림차순(date)) - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시판 작성 날짜 정렬 리스트(date)")
	@GetMapping(value = "/tripboard-date-desc", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripboard_date_list(@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)로 동행 게시글 작성 날짜순 찾기, 성공시 code 1
			tripBoardList = tripBoardService.selectAllOrderByDate(pageNo);
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			// 에러 발생시 code 2
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;

	}

	/*
	 * 동행 게시판 리스트 - 페이징 처리
	 */
	@ApiOperation(value = "동행 게시판 리스트")
	@GetMapping(value = "/tripboard-all-list", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripboard_all_list(@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<TripBoard> tripBoardList = null;
		try {
			// 페이지 번호(default 값 1)로 동행 게시판 리스트, 성공시 code 1
			tripBoardList = tripBoardService.selectAllTb(pageNo);
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			// 에러 발생시 code 2
			e.printStackTrace();
			code = 2;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", tripBoardList);
		return resultMap;

	}

	@ExceptionHandler(FileUploadNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(FileUploadNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
