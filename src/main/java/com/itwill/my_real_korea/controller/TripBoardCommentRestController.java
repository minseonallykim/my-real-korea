package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;
import com.itwill.my_real_korea.service.tripboard.TripBoardCommentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class TripBoardCommentRestController {

	@Autowired
	private TripBoardCommentService tripBoardCommentService;

	// 댓글 추가
	@LoginCheck
	@ApiOperation(value = "댓글 추가")
	@PostMapping(value = "/tripboard-comment-write-action", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_write_action(@RequestBody TripBoardComment tripBoardComment) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoardComment> data = new ArrayList<>();

		try {
			// 댓글 추가 성공 시 code1
			tripBoardCommentService.insertTripBoardComment(tripBoardComment);
			code = 1;
			msg = "성공";

			tripBoardComment = tripBoardCommentService.selectByNo(tripBoardComment.getTCoNo());
			data.add(tripBoardComment);
		} catch (Exception e) {
			// 댓글 추가 실패 시 code2
			e.printStackTrace();
			code = 2;
			msg = "댓글 추가 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;

	}

	// 댓글 수정

	@ApiOperation(value = "댓글 수정")
	@ApiImplicitParam(name = "tCoNo", value = "댓글 번호")
	@PutMapping(value = "/tripboardcommentupdateaction/{tCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoardComment_update_action(@RequestBody TripBoardComment tripBoardComment,
			@PathVariable(value = "tCoNo") int tCoNo) {

		System.out.println(tripBoardComment.getTCoNo());
		System.out.println("tripBoardComment.getTCommentContent()==" + tripBoardComment.getTCommentContent());
		System.out.println(tripBoardComment.getUserId());

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoardComment> data = new ArrayList<TripBoardComment>();
		try {
			TripBoardComment findTripBoardComment = tripBoardCommentService.selectByNo(tripBoardComment.getTCoNo());
			if (findTripBoardComment != null) {
				// 댓글 수정 성공 시 code1
				tripBoardComment.setTCoNo(tripBoardComment.getTCoNo());
				tripBoardCommentService.updateTripBoardComment(tripBoardComment);
				code = 1;
				msg = "성공";
				data.add(tripBoardComment);
			} else {
				// 댓글 수정 실패 시 code2
				code = 2;
				msg = "댓글 수정 실패";
			}
		} catch (Exception e) {
			// 에러 발생 시 code3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 동행게시판 댓글 수정폼

	@ApiOperation(value = "동행게시판 댓글 수정폼")
	@ApiImplicitParam(name = "tCoNo", value = "동행게시판 댓글 번호")
	@GetMapping(value = "tripboardcomment/{tCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tCoCommentModifyForm(@PathVariable(value = "tCoNo") int tCoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";

		List<TripBoardComment> data = new ArrayList<>();
		try {

			TripBoardComment tripBoardComment1 = tripBoardCommentService.selectByNo(tCoNo);
			if (tripBoardComment1 != null) {
				code = 1;
				msg = "성공";

				data.add(tripBoardComment1);
			} else {
				// 실패시 code 2
				code = 2;
				msg = "자유게시판 수정 실패";
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

	// 댓글 삭제
	@LoginCheck
	@ApiOperation(value = "댓글 삭제")
	@ApiImplicitParam(name = "tCoNo", value = "댓글 번호")
	@DeleteMapping(value = "/tripboardcommentdeleteaction/{tCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoardComment_delete_action(@PathVariable(value = "tCoNo") int tCoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoardComment> data = new ArrayList<TripBoardComment>();
		try {
			// 댓글 삭제 성공 시 code1
			int rowCount = tripBoardCommentService.deleteTripBoardComment(tCoNo);
			if (rowCount == 1) {
				code = 1;
				msg = "성공";
			} else {
				// 댓글 삭제 실패 시 code2
				code = 2;
				msg = "댓글 삭제 실패";
				TripBoardComment failTripBoardComment = tripBoardCommentService.selectByNo(tCoNo);
				data.add(failTripBoardComment);
			}
		} catch (Exception e) {
			// 에러 발생 시 code3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		return resultMap;
	}

}
