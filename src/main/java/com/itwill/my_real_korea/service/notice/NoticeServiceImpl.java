package com.itwill.my_real_korea.service.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwill.my_real_korea.dao.notice.NoticeDao;
import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.exception.NoticeNotFoundException;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;

@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	@Override
	public int insertNotice(Notice notice) throws Exception {
		return noticeDao.insertNotice(notice);
	}

	@Override
	public int updateNoticeImg(String nImg, int nNo) throws Exception {
		return noticeDao.updateNoticeImg(nImg, nNo);
	}

	@Override
	public int updateUploadFile(String uploadFile, int nNo) throws Exception {
		return noticeDao.updateUploadFile(uploadFile, nNo);
	}

	@Override
	public int updateNoticeImgNull(int nNo) throws Exception {
		return noticeDao.updateNoticeImgNull(nNo);
	}

	@Override
	public Notice selectByNo(int nNo) throws Exception {
		Notice notice = noticeDao.selectByNo(nNo);
		if (notice == null) {
			throw new NoticeNotFoundException("공지사항이 존재하지 않습니다.");
		}
		return notice;
	}

	/*
	 * 게시글 리스트 보기 (페이징 처리)
	 */
	@Override
	public PageMakerDto<Notice> selectAll(int currentPage) throws Exception {
		// 전체 글의 수
		int totNoticeCount = noticeDao.selectNoticeCount();
		// paging 계산 (PageMaker)
		PageMaker pageMaker = new PageMaker(totNoticeCount, currentPage);
		// 게시글 데이터 얻기
		List<Notice> noticeList = noticeDao.selectAll(pageMaker.getPageBegin(), pageMaker.getPageEnd());
		PageMakerDto<Notice> pageMakerNoticeList = new PageMakerDto<Notice>(noticeList, pageMaker, totNoticeCount);

		return pageMakerNoticeList;
	}

	/*
	 * 최신순 정렬 : 공지사항 리스트 보기 (페이징 처리)
	 */
	@Override
	public PageMakerDto<Notice> selectAllOrderByDateDesc(int currentPage) throws Exception {
		// 전체 글의 수
		int totNoticeCount = noticeDao.selectNoticeCount();
		// paging 계산 (PageMaker)
		PageMaker pageMaker = new PageMaker(totNoticeCount, currentPage);
		// 게시글 데이터 얻기
		List<Notice> noticeList = noticeDao.selectAllOrderByDateDesc(pageMaker.getPageBegin(), pageMaker.getPageEnd());
		PageMakerDto<Notice> pageMakerNoticeList = new PageMakerDto<Notice>(noticeList, pageMaker, totNoticeCount);

		return pageMakerNoticeList;
	}

	/*
	 * 오래된순 정렬 : 공지사항 리스트 보기 (페이징 처리)
	 */
	@Override
	public PageMakerDto<Notice> selectAllOrderByDateAsc(int currentPage) throws Exception {
		// 전체 글의 수
		int totNoticeCount = noticeDao.selectNoticeCount();
		// paging 계산 (PageMaker)
		PageMaker pageMaker = new PageMaker(totNoticeCount, currentPage);
		// 게시글 데이터 얻기
		List<Notice> noticeList = noticeDao.selectAllOrderByDateAsc(pageMaker.getPageBegin(), pageMaker.getPageEnd());
		PageMakerDto<Notice> pageMakerNoticeList = new PageMakerDto<Notice>(noticeList, pageMaker, totNoticeCount);

		return pageMakerNoticeList;
	}

	/*
	 * 조회수 높은순 정렬 : 공지사항 리스트 보기 (페이징 처리)
	 */
	@Override
	public PageMakerDto<Notice> selectAllOrderByReadcount(int currentPage) throws Exception {
		// 전체 글의 수
		int totNoticeCount = noticeDao.selectNoticeCount();
		// paging 계산 (PageMaker)
		PageMaker pageMaker = new PageMaker(totNoticeCount, currentPage);
		// 게시글 데이터 얻기
		List<Notice> noticeList = noticeDao.selectAllOrderByReadcount(pageMaker.getPageBegin(), pageMaker.getPageEnd());
		PageMakerDto<Notice> pageMakerNoticeList = new PageMakerDto<Notice>(noticeList, pageMaker, totNoticeCount);

		return pageMakerNoticeList;
	}

	/*
	 * 키워드로 검색된 게시글 리스트 보기 (페이징 처리)
	 */
	@Override
	public PageMakerDto<Notice> selectSearchNoticeList(int currentPage, String keyword) throws Exception {
		// 전체 글의 수
		int totNoticeCount = noticeDao.selectSearchCount(keyword);
		// paging 계산 (PageMaker)
		PageMaker pageMaker = new PageMaker(totNoticeCount, currentPage);
		// 게시글 데이터 얻기
		List<Notice> noticeList = noticeDao.selectSearchNoticeList(pageMaker.getPageBegin(), pageMaker.getPageEnd(),
				keyword);
		PageMakerDto<Notice> pageMakerNoticeList = new PageMakerDto<Notice>(noticeList, pageMaker, totNoticeCount);

		return pageMakerNoticeList;
	}

	/*
	 * 게시글 title 출력 설정
	 */
	@Override
	public String getTitleString(Notice notice) {
		StringBuilder title = new StringBuilder(128);
		String t = notice.getNTitle();
		if (t.length() > 15) {
			t = String.format("%s...", t.substring(0, 15));
		}

		title.append(t.replace(" ", "&nbsp;"));
		return title.toString();
	}

	@Override
	public int deleteNotice(int nNo) throws Exception {
		return noticeDao.deleteNotice(nNo);
	}

	@Override
	public int updateNotice(Notice notice) throws Exception {
		return noticeDao.updateNotice(notice);
	}

	@Override
	public int increaseReadCount(int nNo) throws Exception {
		return noticeDao.increaseReadCount(nNo);
	}

	@Override
	public int selectNoticeCount() throws Exception {
		return noticeDao.selectNoticeCount();
	}

	@Override
	public int selectSearchCount(String keyword) throws Exception {
		return noticeDao.selectSearchCount(keyword);
	}

}
