package com.itwill.my_real_korea.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.my_real_korea.dto.user.UserImg;

//@SpringBootTest
//@Transactional
class UserImgServiceImplTest {
	@Autowired
	private UserImgService userImgService;
	
	//1. 회원 이미지 등록
	@Test
	void testCreateUserImg() throws Exception {
		UserImg userImg = new UserImg(21,"new.jpg","admin");
		System.out.println(">> created : "+userImgService.createUserImg(userImg));
	}
	
	//2. 회원 이미지 찾기
	@Test
	void testFindUserImg() throws Exception {
		System.out.println(">> findUserImg : "+userImgService.findUserImg("admin"));
	}
	
	//3. 회원 이미지 수정
	@Test
	void testUpdateUserImg() throws Exception {
		UserImg userImg = userImgService.findUserImg("admin");
		userImg.setUserImgUrl("change.jpg");
		System.out.println(">> updated : "+userImgService.updateUserImg(userImg));
	}
	
	//4. 회원 이미지 삭제
	@Test
	void testRemoveUserImg() throws Exception {
		int removeRowCount = userImgService.removeUserImg(11);
		System.out.println(">> removed rowCount : "+removeRowCount);
	}


}
