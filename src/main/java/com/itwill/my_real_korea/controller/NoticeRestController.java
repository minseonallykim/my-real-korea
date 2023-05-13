package com.itwill.my_real_korea.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.exception.IsNotAdminException;
import com.itwill.my_real_korea.service.notice.NoticeService;
import com.itwill.my_real_korea.util.FileUploadNotFoundException;
import com.itwill.my_real_korea.util.FileUploadService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class NoticeRestController {

	private final FileUploadService storageService;

	@Autowired
	private NoticeService noticeService;

	// NoticeRestController 객체 생성 시 storaeService 사용, 파일 업로드 기능 구현가능하도록
	@Autowired
	public NoticeRestController(FileUploadService storageService) {
		this.storageService = storageService;
	}

	// HTML <img>에 이미지 출력
	@ResponseBody // 이 메소드는 HTTP응답의 body로 사용될 객체를 반환한다.
	@GetMapping(value = "/noticeimages/{filename}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
			MediaType.IMAGE_GIF_VALUE })
	public Resource showImage(@PathVariable String filename) throws MalformedURLException {
		// UrlResource로 이미지 파일을 읽어서 @ResponseBody로 이미지 바이너리 반환
		return new UrlResource("file:" + storageService.getFullPath(filename));
	}

	/*
	 * 최신순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	@ApiOperation(value = "공지사항 리스트 최신순")
	@GetMapping(value = "/notice-date-desc", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_list_date_desc(@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<Notice> noticeList = null;
		try {
			// 페이지 번호(default 값 1)로 공지사항 리스트 최신순 찾기, 성공시 code 1
			noticeList = noticeService.selectAllOrderByDateDesc(pageNo);
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
		resultMap.put("data", noticeList);
		return resultMap;
	}

	/*
	 * 오래된순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	@ApiOperation(value = "공지사항 리스트 오래된순")
	@GetMapping(value = "/notice-date-asc", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_list_date_asc(@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<Notice> noticeList = null;
		try {
			// 페이지 번호(default 값 1)로 공지사항 리스트 오래된순 찾기, 성공시 code 1
			noticeList = noticeService.selectAllOrderByDateAsc(pageNo);
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
		resultMap.put("data", noticeList);
		return resultMap;
	}

	/*
	 * 조회수 높은순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	@ApiOperation(value = "공지사항 리스트 조회순")
	@GetMapping(value = "/notice-readcount", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_list_readcount(@RequestParam(required = false, defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<Notice> noticeList = null;
		try {
			// 페이지 번호(default 값 1)로 공지사항 리스트 조회순 찾기, 성공시 code 1
			noticeList = noticeService.selectAllOrderByReadcount(pageNo);
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
		resultMap.put("data", noticeList);
		return resultMap;
	}

	/*
	 * 공지사항 게시판 title 키워드로 검색
	 */
	@ApiOperation(value = "공지사항 검색결과 리스트")
	@GetMapping(value = "/notice-search/{keyword}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_search_list(@RequestParam(required = false, defaultValue = "1") int pageNo,
			@PathVariable String keyword) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		PageMakerDto<Notice> noticeList = null;
		try {
			// 페이지 번호(default 값 1)와 검색 keyword로 공지사항 검색결과 리스트 찾기, 성공시 code 1
			noticeList = noticeService.selectSearchNoticeList(pageNo, keyword);
			if (noticeList.getTotRecordCount() != 0 && noticeList != null) {
				code = 1;
				msg = "성공";
			} else {
				// 검색 결과 없을시 code 2
				code = 2;
				msg = "해당 키워드에 해당하는 공지사항이 없습니다.";
			}
		} catch (Exception e) {
			// 에러 발생시 code 3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", noticeList);
		return resultMap;
	}

	/*
	 * 공지사항 게시글 추가
	 */
	@AdminCheck
	@LoginCheck
	@ApiOperation(value = "공지사항 글쓰기")
	@PostMapping(value = "/notice", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_write_action(@ModelAttribute Notice notice,
			@RequestParam(name = "file", required = false) MultipartFile file) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Notice> data = new ArrayList<Notice>();

		try {
			// 공지사항 글쓰기, 성공시 code 1
			notice.setNImg("");
			// noticeService.updateNoticeImgNull(notice.getNNo());
			noticeService.insertNotice(notice);

			// 파일업로드 처리하는 부분
			if (file != null) {
				// 선택된 파일이 있다면, 파일 저장
				storageService.store(file);
				// 업로드 된 파일로 uploadFile update
				noticeService.updateUploadFile(file.getOriginalFilename(), notice.getNNo());
				System.out.println(">>>>>>>>>>>>>>" + file.getOriginalFilename());
			}
			// 이미지 있다면 nImg update
			if (notice.getNImg() != null) {
				noticeService.updateNoticeImg(notice.getNImg(), notice.getNNo());
			}
			// 공지사항 글쓰기 후 그 공지사항을 찾아서 데이터에 붙여줌
			notice = noticeService.selectByNo(notice.getNNo());
			code = 1;
			msg = "성공";
			data.add(notice);

		} catch (Exception e) {
			// 실패 시 code 2
			e.printStackTrace();
			code = 2;
			msg = "공지사항 글쓰기 실패";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("data", data);

		return resultMap;
	}

	// 공지사항 게시글 삭제
	@AdminCheck
	@LoginCheck
	@ApiOperation(value = "공지사항 삭제")
	@ApiImplicitParam(name = "nNo", value = "공지사항 번호")
	@DeleteMapping(value = "/notice/{nNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_delete_action(@PathVariable(value = "nNo") int nNo, HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Notice> data = new ArrayList<Notice>();
		try {
			// nNo로 공지사항 삭제, 성공시 code 1
			int rowCount = noticeService.deleteNotice(nNo);
			if (rowCount != 0) {
				code = 1;
				msg = "성공";
			} else {
				// 실패시 code 2
				code = 2;
				msg = "공지사항 삭제 실패";
				// 삭제 실패한 nNo 데이터에 붙여줌
				Notice failNotice = noticeService.selectByNo(nNo);
				data.add(failNotice);
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
	 * 공지사항 게시글 내용 수정
	 */
	@AdminCheck
	@LoginCheck
	@ApiOperation(value = "공지사항 수정")
	@ApiImplicitParam(name = "nNo", value = "공지사항 번호")
	@PutMapping(value = "/notice/{nNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_modify_action(@PathVariable(value = "nNo") int nNo, @ModelAttribute Notice notice,
			@RequestParam(name = "file", required = false) MultipartFile file) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Notice> data = new ArrayList<Notice>();
		try {
			// nNo로 공지사항 1개 찾기, 수정 성공시 code 1
			Notice findNotice = noticeService.selectByNo(nNo);
			if (findNotice != null) {
				notice.setNNo(nNo);
				// 파일업로드 처리하는 부분
				if (file != null) {
					// 선택된 파일이 있다면, 파일 저장
					storageService.store(file);
					// 업로드 된 파일로 uploadFile update, 기존의 nImg null 만들기
					noticeService.updateNoticeImgNull(nNo);
					noticeService.updateUploadFile(file.getOriginalFilename(), nNo);
					System.out.println(">>>>>>>>>>>>>>" + file.getOriginalFilename());
				}
				noticeService.updateNotice(notice);
				code = 1;
				msg = "성공";
				// 수정 성공한 공지사항 객체 데이터에 붙여줌
				data.add(notice);
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

	/*
	 * 공지사항 게시글 번호(boardno)로 해당 게시글 보기 => modify action 이후 보여주는 용도
	 */
	@ApiOperation(value = "공지사항 상세보기")
	@ApiImplicitParam(name = "nNo", value = "공지사항 번호")
	@GetMapping(value = "/notice/{nNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> notice_detail(@PathVariable(value = "nNo") int nNo, HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<Notice> data = new ArrayList<Notice>();
		User loginUser = null;

		try {
			if (session != null) {
				loginUser = (User) session.getAttribute("loginUser");
			}
			// nNo로 공지사항 1개 찾기, 성공시 code 1
			Notice notice = noticeService.selectByNo(nNo);
			if (notice != null) {
				// 공지사항 게시글 조회수 1 증가
				noticeService.increaseReadCount(nNo);
				code = 1;
				data.add(notice);
			} else {
				// 실패 시 code 2
				code = 2;
				msg = "해당 공지사항이 존재하지 않습니다.";
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
		resultMap.put("loginUser", loginUser);

		return resultMap;
	}

	@ExceptionHandler(FileUploadNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(FileUploadNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
