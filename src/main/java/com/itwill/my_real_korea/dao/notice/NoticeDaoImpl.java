package com.itwill.my_real_korea.dao.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.mapper.NoticeMapper;

@Repository
public class NoticeDaoImpl implements NoticeDao {

	@Autowired
	private NoticeMapper noticeMapper;

	public NoticeDaoImpl() {
		System.out.println("NoticeDaoImpl 기본생성자 호출");
	}

	public NoticeMapper getNoticeMapper() {
		return noticeMapper;
	}

	public void setNoticeMapper(NoticeMapper noticeMapper) {
		System.out.println(">>> noticeDaoImpl():setNoticeMappper()호출");
		this.noticeMapper = noticeMapper;
	}

	@Override
	public int insertNotice(Notice notice) throws Exception {
		return noticeMapper.insertNotice(notice);
	}

	@Override
	public int updateNoticeImg(String nImg, int nNo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("nImg", nImg);
		map.put("nNo", nNo);
		return noticeMapper.updateNoticeImg(map);
	}

	@Override
	public int updateUploadFile(String uploadFile, int nNo) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("uploadFile", uploadFile);
		map.put("nNo", nNo);
		return noticeMapper.updateUploadFile(map);
	}

	@Override
	public int updateNoticeImgNull(int nNo) throws Exception {
		return noticeMapper.updateNoticeImgNull(nNo);
	}

	@Override
	public Notice selectByNo(int nNo) throws Exception {
		return noticeMapper.selectByNo(nNo);
	}

	@Override
	public List<Notice> selectAll(int pageStart, int pageEnd) throws Exception {
		Map<String, Object> pageMap = new HashMap<>();
		pageMap.put("pageStart", pageStart);
		pageMap.put("pageEnd", pageEnd);

		return noticeMapper.selectAll(pageMap);
	}

	@Override
	public List<Notice> selectAllOrderByDateDesc(int pageStart, int pageEnd) throws Exception {
		Map<String, Object> pageMap = new HashMap<>();
		pageMap.put("pageStart", pageStart);
		pageMap.put("pageEnd", pageEnd);
		return noticeMapper.selectAllOrderByDateDesc(pageMap);
	}

	@Override
	public List<Notice> selectAllOrderByDateAsc(int pageStart, int pageEnd) throws Exception {
		Map<String, Object> pageMap = new HashMap<>();
		pageMap.put("pageStart", pageStart);
		pageMap.put("pageEnd", pageEnd);
		return noticeMapper.selectAllOrderByDateAsc(pageMap);
	}

	@Override
	public List<Notice> selectAllOrderByReadcount(int pageStart, int pageEnd) throws Exception {
		Map<String, Object> pageMap = new HashMap<>();
		pageMap.put("pageStart", pageStart);
		pageMap.put("pageEnd", pageEnd);
		return noticeMapper.selectAllOrderByReadcount(pageMap);
	}

	@Override
	public int deleteNotice(int nNo) throws Exception {
		return noticeMapper.deleteNotice(nNo);
	}

	@Override
	public int updateNotice(Notice notice) throws Exception {
		return noticeMapper.updateNotice(notice);
	}

	@Override
	public int increaseReadCount(int nNo) throws Exception {
		return noticeMapper.increaseReadCount(nNo);
	}

	@Override
	public int selectNoticeCount() throws Exception {
		return noticeMapper.selectNoticeCount();
	}

	@Override
	public int selectSearchCount(String keyword) throws Exception {
		return noticeMapper.selectSearchCount(keyword);
	}

	@Override
	public List<Notice> selectSearchNoticeList(int pageStart, int pageEnd, String keyword) throws Exception {
		Map<String, Object> pageMap = new HashMap<>();
		pageMap.put("pageStart", pageStart);
		pageMap.put("pageEnd", pageEnd);
		pageMap.put("keyword", keyword);

		return noticeMapper.selectSearchNoticeList(pageMap);
	}

}
