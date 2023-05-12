package com.itwill.my_real_korea.dao.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.my_real_korea.dto.user.UserImg;

//@Transactional
//@SpringBootTest
class UserImgDaoImplTest {
	@Autowired
	private UserImgDao userImgDao;

	//1. 회원 이미지 등록
	@Test
	void testCreateUserImg() throws Exception {
		UserImg createUserImg = new UserImg(0, "test4.png", "test44");
		System.out.println(">> created : "+userImgDao.createUserImg(createUserImg));
	}
	
	//2. 회원 이미지 찾기
	@Test
	void testFindUserImg() throws Exception {
		System.out.println(userImgDao.findUserImg("user2"));
	}

	//3. 회원 이미지 수정
	@Test
	void testUpdateUserImg() throws Exception {
		UserImg updateUserImg = new UserImg(0, "change1.png", "user1");
		System.out.println(userImgDao.updateUserImg(updateUserImg));
	}
	
	//4. 회원 이미지 삭제
	@Test
	void testRemoveUserImg() throws Exception {
		System.out.println(userImgDao.removeUserImg(11));
	}


}
