package com.itwill.my_real_korea.dao.notice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dao.notice.NoticeDao;
import com.itwill.my_real_korea.dto.notice.Notice;

//@SpringBootApplication
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class NoticeDaoImplTest {

	@Autowired
	private NoticeDao noticeDao;

	@Test
	void testInsertNotice() throws Exception {
		int rowCount = noticeDao.insertNotice(new Notice(0, "공지테스트1", "내용테스트1", null, 0, "", "aaa111"));
		assertEquals(rowCount, 1);
	}

	@Disabled
	@Test
	void testUpdateNoticeImg() throws Exception {
		int rowCount = noticeDao.updateNoticeImg("image.png", 4);
		assertEquals(rowCount, 1);
	}

	@Disabled
	@Test
	void testUpdateUploadFile() throws Exception {
		int rowCount = noticeDao.updateUploadFile("file.png", 4);
		assertEquals(rowCount, 1);
	}

	@Disabled
	@Test
	void testSelectByNo() throws Exception {
		assertNotNull(noticeDao.selectByNo(1));
		System.out.println(noticeDao.selectByNo(1));
	}

	@Disabled
	@Test
	void testSelectAll() throws Exception {
		assertNotNull(noticeDao.selectAll(1, 10));
		List<Notice> noticeList = noticeDao.selectAll(1, 10);
		System.out.println(noticeList);
	}

	@Disabled
	@Test
	void testselectAllOrderByDateDesc() throws Exception {
		assertNotNull(noticeDao.selectAll(1, 10));
		List<Notice> noticeList = noticeDao.selectAllOrderByDateDesc(1, 10);
		System.out.println(noticeList);
	}

	@Disabled
	@Test
	void testselectAllOrderByDateAsc() throws Exception {
		assertNotNull(noticeDao.selectAll(1, 10));
		List<Notice> noticeList = noticeDao.selectAllOrderByDateAsc(1, 10);
		System.out.println(noticeList);
	}

	@Disabled
	@Test
	void testselectAllOrderByReadcount() throws Exception {
		assertNotNull(noticeDao.selectAll(1, 10));
		List<Notice> noticeList = noticeDao.selectAllOrderByReadcount(1, 10);
		System.out.println(noticeList);
	}

	@Disabled
	@Test
	void testDeleteNotice() throws Exception {
		int rowCount = noticeDao.deleteNotice(4);
		assertEquals(rowCount, 1);
	}

	@Disabled
	@Test
	void testUpdateNotice() throws Exception {
		int rowCount = noticeDao.updateNotice(new Notice(1, "공지수정1", "내용수정1", null, 0, "", "user1"));
		assertEquals(rowCount, 1);
	}

	@Disabled
	@Test
	void testIncreaseReadCount() throws Exception {
		int rowCount = noticeDao.increaseReadCount(3);
		assertEquals(rowCount, 1);
	}

	@Disabled
	void testSelectNoticeCount() throws Exception {
		assertNotNull(noticeDao.selectNoticeCount());
	}

	@Disabled
	@Test
	void testSelectSearchCount() throws Exception {
		assertNotNull(noticeDao.selectSearchCount("공지"));
		System.out.println(noticeDao.selectSearchCount("공지"));
	}

	@Disabled
	@Test
	void testSelectSearchNoticeList() throws Exception {
		assertNotNull(noticeDao.selectSearchNoticeList(1, 10, "공지"));
		List<Notice> noticeList = noticeDao.selectSearchNoticeList(1, 10, "공지");
		System.out.println(noticeList);
	}

}
