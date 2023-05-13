package com.itwill.my_real_korea.dao.notice;

import java.util.List;
import java.util.Map;

import com.itwill.my_real_korea.dto.notice.Notice;

public interface NoticeDao {

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

	/*
	 * 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	List<Notice> selectAll(int pageStart, int pageEnd) throws Exception;

	/*
	 * 최신순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	List<Notice> selectAllOrderByDateDesc(int pageStart, int pageEnd) throws Exception;

	/*
	 * 오래된순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	List<Notice> selectAllOrderByDateAsc(int pageStart, int pageEnd) throws Exception;

	/*
	 * 조회수 높은순 정렬 : 공지사항 리스트 보기 (게시글 시작번호, 게시글 끝번호) - 페이징 처리
	 */
	List<Notice> selectAllOrderByReadcount(int pageStart, int pageEnd) throws Exception;

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

	/*
	 * 공지사항 게시판 title 키워드로 검색
	 */
	List<Notice> selectSearchNoticeList(int pageStart, int pageEnd, String keyword) throws Exception;

}
