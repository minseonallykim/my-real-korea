package com.itwill.my_real_korea.controller;

import com.itwill.my_real_korea.dto.freeboard.FreeBoardComment;
import com.itwill.my_real_korea.service.freeboard.FreeBoardCommentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FreeBoardCommentRestController {
	@Autowired
	private FreeBoardCommentService freeBoardCommentService;

	@ApiOperation(value = "자유게시판 댓글 수정폼")
	@ApiImplicitParam(name = "fCoNo", value = "자유게시판 댓글 번호")
	@GetMapping(value = "freeboardcomment/{fCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> fCoCommentModifyForm(@PathVariable(value = "fCoNo") int fCoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";

		List<FreeBoardComment> data = new ArrayList<>();
		try {
			// fBoNo로 자유게시판 1개 찾기, 수정 성공시 code 1
			FreeBoardComment freeBoardComment1 = freeBoardCommentService.selectByfCoNo(fCoNo);
			if (freeBoardComment1 != null) {
				code = 1;
				msg = "성공";
				// 수정 성공한 자유게시판 객체 데이터에 붙여줌
				data.add(freeBoardComment1);
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

	@ApiOperation(value = "자유게시판 수정액션")
	@ApiImplicitParam(name = "fCoNo", value = "공지사항 번호")
	@PutMapping(value = "/freeboardcomment/{fCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> freeboardcomment_modify_action(@RequestBody FreeBoardComment freeBoardComment) {

		System.out.println(freeBoardComment.getFCoNo());
		System.out.println("freeBoardComment.getFCommentContent()==" + freeBoardComment.getFCommentContent());
		System.out.println(freeBoardComment.getUserId());

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoardComment> data = new ArrayList<>();
		try {
			// nNo로 공지사항 1개 찾기, 수정 성공시 code 1
			FreeBoardComment findFreeBoardComment = freeBoardCommentService.selectByfCoNo(freeBoardComment.getFCoNo());
			if (findFreeBoardComment != null) {
				freeBoardComment.setFCoNo(freeBoardComment.getFCoNo());
				freeBoardCommentService.updateComment(freeBoardComment);
				code = 1;
				msg = "성공";
				// 수정 성공한 공지사항 객체 데이터에 붙여줌
				data.add(freeBoardComment);
			} else {
				// 실패시 code 2
				code = 2;
				msg = "공지사항 수정 실패";
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

	@ApiOperation(value = "자유게시판 댓글 삭제")
	@ApiImplicitParam(name = "fCoNo", value = "자유게시판 댓글 번호")
	@DeleteMapping(value = "freeboardcomment/{fCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> freeBoardDeleteAction(@PathVariable(value = "fCoNo") int fCoNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<FreeBoardComment> data = new ArrayList<>();
		try {
			// nNo로 공지사항 삭제, 성공시 code 1
			int rowCount = freeBoardCommentService.deleteComment(fCoNo);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "공지사항 삭제 실패";
				// 삭제 실패한 nNo 데이터에 붙여줌
				FreeBoardComment failFreeBoardComment = freeBoardCommentService.selectByfCoNo(fCoNo);
				data.add(failFreeBoardComment);
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
