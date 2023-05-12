package com.itwill.my_real_korea.dao.chat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.chat.ChatMsg;

//@SpringBootApplication
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class ChatMsgDaoImplTest {

	@Autowired
	private ChatMsgDao chatMsgDao;
	
	@Disabled
	@Test
	void testSelectByRoomNo() {
		assertNotNull(chatMsgDao.selectChatByRoomName("채팅")); 
		System.out.println(chatMsgDao.selectChatByRoomName("채팅"));
	}

	@Disabled
	@Test
	void testSelectByMsgNo() {
		assertNotNull(chatMsgDao.selectByMsgNo(2)); 
		System.out.println(chatMsgDao.selectByMsgNo(2));
	}
	
	@Disabled
	@Test
	void testSelectNotReadMsg() {
		assertNotNull(chatMsgDao.selectNotReadMsg(1, "kms1")); 
		System.out.println(chatMsgDao.selectNotReadMsg(1, "kms1"));
	}
	
	@Disabled
	@Test
	void testCountNotReadMsg() {
		int rowCount = chatMsgDao.countNotReadMsg(1, "kms1");
		assertNotEquals(rowCount, 0);
		System.out.println(chatMsgDao.countNotReadMsg(1, "kms1"));
	}
	
	
	@Test
	void testSelectAllNotReadMsg() {
		assertNotNull(chatMsgDao.selectAllNotReadMsg("kms2"));
		System.out.println(chatMsgDao.selectAllNotReadMsg("kms2"));
	}
	
	@Disabled
	@Test
	void testCountAllNotReadMsg() {
		int rowCount = chatMsgDao.countAllNotReadMsg("kms1");
		assertNotEquals(rowCount, 0);
		System.out.println(chatMsgDao.countAllNotReadMsg("kms1"));
	}
	
	@Disabled
	@Test
	void testUpdateReadMsg() {
		int rowCount = chatMsgDao.updateReadMsg(1, "kms1");
		assertNotEquals(rowCount, 0);
	}
	
	@Disabled
	@Test
	void testDeleteChatMsg() {
		int rowCount = chatMsgDao.deleteChatMsg(3);
		assertNotEquals(rowCount, 0);
	}
	
	@Disabled
	@Test
	void testUpdateDeletedMsg() {
		int rowCount = chatMsgDao.updateDeletedMsg(2);
		assertNotEquals(rowCount, 0);
	}
	
	@Disabled
	@Test
	void testInsertChatMsg() {
		int rowCount = chatMsgDao.insertChatMsg(new ChatMsg(0, "하이이", null, 0, "채팅방1", "kms2"));
		assertEquals(rowCount, 1);
	}

}
