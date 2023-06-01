package com.itwill.my_real_korea.controller;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.freeboard.FreeBoard;
import com.itwill.my_real_korea.dto.freeboard.FreeBoardComment;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.freeboard.FreeBoardCommentService;
import com.itwill.my_real_korea.service.freeboard.FreeBoardService;
import com.itwill.my_real_korea.util.PageMakerDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FreeBoardRestController {
	private FreeBoardService freeBoardService;
	private FreeBoardCommentService freeBoardCommentService;
	private CityService cityService;

	@Autowired
	public FreeBoardRestController(FreeBoardService freeBoardService, FreeBoardCommentService freeBoardCommentService,
			CityService cityService) {
		this.freeBoardService = freeBoardService;
		this.freeBoardCommentService = freeBoardCommentService;
		this.cityService = cityService;
	}

	@ApiOperation(value = "자유게시판 리스트 최신순")
	@GetMapping(value = "/fBoList", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fBoListFBoNoDesc(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = true) Integer cityNo) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<FreeBoard> freeBoardListPage = null;
		List<FreeBoard> data1 = new ArrayList<FreeBoard>();
		try {
			freeBoardListPage = freeBoardService.selectFreeBoardCityList(pageNo, cityNo);
			System.out.println(freeBoardListPage);
			for (FreeBoard freeBoard : freeBoardListPage.getItemList()) {
				int commentCount = freeBoardCommentService.selectByfBoNo(freeBoard.getFBoNo()).size();
				freeBoard.setCommentCount(commentCount);
				data1.add(freeBoard);

			}
			if (freeBoardListPage.getPageMaker().getTotCount() != 0 && freeBoardListPage != null) {
				code = 1;
				msg = "성공";
			} else {
				code = 2;
				msg = "해당 지역과 일치하는 게시글이 없습니다.";
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("errorCode", -1);
			resultMap.put("errorMsg", "관리자에게 문의하세요");
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data1);
		resultMap.put("freeBoardListPage", freeBoardListPage);
		return resultMap;
	}

	/*
	 * 자유게시판 title 키워드로 검색
	 */
	@ApiOperation(value = "자유게시판 검색결과 리스트")
	@GetMapping(value = "/freeBoard-search", produces = "application/json;charset=UTF-8")
	public Map<String, Object> freeBoardSearchList(@RequestParam(required = false, defaultValue = "1") int pageNo,
			@RequestParam(required = true) String keyword) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<FreeBoard> freeBoardList = null;
		List<FreeBoard> freeBoard = new ArrayList<>();
		try {
			// 페이지 번호(default 값 1)와 검색 keyword로 자유게시판 검색결과 리스트 찾기, 성공시 code 1
			freeBoardList = freeBoardService.selectSearchFreeBoardList(pageNo, keyword);
			List<FreeBoard> tempFreeBoardList = freeBoardList.getItemList();
			for (FreeBoard freeboard : tempFreeBoardList) {
				int commentCount = freeBoardCommentService.selectByfBoNo(freeboard.getFBoNo()).size();
				freeboard.setCommentCount(commentCount);
				freeBoard.add(freeboard);
			}
			if (freeBoardList.getPageMaker().getTotCount() != 0 && freeBoardList != null) {
				code = 1;
				msg = "성공";
			} else {
				// 검색 결과 없을시 code 2
				code = 2;
				msg = "해당 키워드에 해당하는 게시글이 없습니다.";
			}
		} catch (Exception e) {
			// 에러 발생시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", freeBoardList);
		resultMap.put("freeBoard", freeBoard);
		return resultMap;
	}

	// 상세보기
	@ApiOperation(value = "자유게시판 상세보기")
	@ApiImplicitParam(name = "fBoNo", value = "자유게시판 번호")
	@GetMapping(value = "/fBoDetail/{fBoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fBoDetail(@PathVariable(value = "fBoNo") int fBoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoard> data = new ArrayList<FreeBoard>();
		List<FreeBoardComment> data2 = new ArrayList<FreeBoardComment>();
		try {
			// fBoNo로 게시글 1개 찾기, 성공시 code 1
			FreeBoard freeBoard = freeBoardService.selectByNo(fBoNo);
			List<FreeBoardComment> freeBoardCommentList = freeBoardCommentService.selectByfBoNo(fBoNo);
			if (freeBoard != null) {
				// 자유게시판 게시글 조회수 1 증가
				freeBoardService.increaseReadCount(fBoNo);
				code = 1;
				data.add(freeBoard);
				data2 = freeBoardCommentList;

			} else {
				// 실패 시 code 2
				code = 2;
				msg = "해당 게시물이 존재하지 않습니다.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 에러 발생 시 code 3
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		resultMap.put("data2", data2);

		return resultMap;
	}

	@ApiOperation(value = "자유게시판 글쓰기")
	@PostMapping(value = "/freeboard", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fBoWriteAction(@ModelAttribute FreeBoard freeBoard, @RequestParam Integer cityNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoard> data = new ArrayList<FreeBoard>();
		System.out.println(freeBoard.getFBoContent());
		System.out.println(freeBoard.getFBoTitle());
		System.out.println(cityNo);

		try {

			City city = cityService.findByCityNo(cityNo);

			System.out.println(city);
			freeBoard.setCity(city);
			// 자유게시판 글쓰기, 성공시 code 1
			freeBoardService.insertBoard(freeBoard);
			code = 1;
			msg = "성공";
			// 자유게시판 글쓰기 후 자유게시판 찾아서 데이터에 붙여줌
			freeBoard = freeBoardService.selectByNo(freeBoard.getFBoNo());
			data.add(freeBoard);
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "자유게시판 글쓰기 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 자유게시판 등록된 글 수정
	@ApiOperation(value = "자유게시판 글 수정")
	@ApiImplicitParam(name = "fBoNo", value = "자유게시판 번호")
	@PutMapping(value = "/freeboardModify", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fBoModifyAction(@ModelAttribute FreeBoard freeBoard, @RequestParam Integer cityNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoard> data = new ArrayList<FreeBoard>();
		System.out.println(freeBoard.getFBoContent());
		System.out.println(freeBoard.getFBoTitle());
		System.out.println(cityNo);

		try {
			City city = cityService.findByCityNo(cityNo);
			freeBoard.setCity(city);
			// 자유게시판 글쓰기, 성공시 code 1
			freeBoardService.updateFreeBoard(freeBoard);
			code = 1;
			msg = "성공";
			// 자유게시판 글쓰기 후 자유게시판 찾아서 데이터에 붙여줌
			freeBoard = freeBoardService.selectByNo(freeBoard.getFBoNo());
			data.add(freeBoard);
		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "자유게시판 글쓰기 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);
		return resultMap;
	}

	// 자유게시판 게시글 삭제
	@LoginCheck
	@ApiOperation(value = "자유게시판 글 삭제")
	@ApiImplicitParam(name = "fBoNo", value = "자유게시판 번호")
	@DeleteMapping(value = "/fBoDelete/{fBoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fBoDeleteAction(@PathVariable(value = "fBoNo") int fBoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoard> data = new ArrayList<FreeBoard>();
		try {
			// fBoNo로 자유게시판 삭제, 성공시 code 1
			int rowCount = freeBoardService.deleteFreeBoard(fBoNo);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "자유게시판 글 삭제 실패";
				// 삭제 실패한 fBoNo 데이터에 붙여줌
				FreeBoard failFreeBoard = freeBoardService.selectByNo(fBoNo);
				data.add(failFreeBoard);
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
}
