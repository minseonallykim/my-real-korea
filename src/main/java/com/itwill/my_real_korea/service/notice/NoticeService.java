package com.itwill.my_real_korea.service.notice;

import java.util.List;
import java.util.Map;

import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.util.PageMakerDto;

public interface NoticeService {
	/*
	 * 공지사항 게시글 추가
	 */
	int insertNotice(Notice notice) throws Exception;

	/*
	 * 공지사항 게시글 이미지 변경(추가)
	 */
	int updateNoticeImg(String nImg, int nNo) throws Exception;

	/*
	 * 공지사항 게시글 업로드된 파일 변경(추가)
	 */
	int updateUploadFile(String uploadFile, int nNo) throws Exception;

	/*
	 * 공지사항 게시글 이미지 null 로 만들기
	 */
	int updateNoticeImgNull(int nNo) throws Exception;

	/*
	 * 공지사항 게시글 번호(boardno)로 해당 게시글 보기
	 */
	Notice selectByNo(int nNo) throws Exception;

	// 페이지메이커 사용

	/*
	 * 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	PageMakerDto<Notice> selectAll(int currentPage) throws Exception;

	/*
	 * 최신순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	PageMakerDto<Notice> selectAllOrderByDateDesc(int currentPage) throws Exception;

	/*
	 * 오래된순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	PageMakerDto<Notice> selectAllOrderByDateAsc(int currentPage) throws Exception;

	/*
	 * 조회수 높은순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	PageMakerDto<Notice> selectAllOrderByReadcount(int currentPage) throws Exception;

	/*
	 * 공지사항 게시판 title 키워드로 검색
	 */
	PageMakerDto<Notice> selectSearchNoticeList(int currentPage, String keyword) throws Exception;

	/*
	 * 게시글 title 출력 설정
	 */
	String getTitleString(Notice notice) throws Exception;

	/*
	 * 공지사항 게시글 삭제
	 */
	int deleteNotice(int nNo) throws Exception;

	/*
	 * 공지사항 게시글 내용 수정
	 */
	int updateNotice(Notice notice) throws Exception;

	/*
	 * 공지사항 게시글 조회수 1 증가
	 */
	int increaseReadCount(int nNo) throws Exception;

	/*
	 * 공지사항 게시글 총 개수 조회
	 */
	int selectNoticeCount() throws Exception;

	/*
	 * 검색된 게시글 총 개수 조회
	 */
	int selectSearchCount(String keyword) throws Exception;

}
