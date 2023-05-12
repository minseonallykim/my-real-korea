package com.itwill.my_real_korea.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.my_real_korea.dto.user.UserAddInfo;
//@Transactional
//@SpringBootTest
class UserAddInfoServiceImplTest {
	@Autowired
	private UserAddInfoService userAddInfoService;

	//1. 회원 추가 정보 등록
	@Test
	void testCreateUserAddInfo() throws Exception {
		UserAddInfo newAddInfo = new UserAddInfo("테스트", 0, 0, "user1");
		System.out.println(">> created :"+userAddInfoService.createUserAddInfo(newAddInfo));
	}
	
	//2. 회원 추가 정보 보기
	@Test
	void testFindUserAddInfo() throws Exception {
		System.out.println(">> find :"+userAddInfoService.findUserAddInfo("user1"));
	}

	//3. 회원 추가 정보 수정
	@Test
	void testUpdateUserAddInfo() throws Exception {
		UserAddInfo updateAddInfo = userAddInfoService.findUserAddInfo("user1");
		updateAddInfo.setIntroduce("msg2");
		updateAddInfo.setAlcohol(0);
		updateAddInfo.setSmoking(0);
		System.out.println(">> updated :"+userAddInfoService.updateUserAddInfo(updateAddInfo));
	}
	
	//4. 회원 추가 정보 삭제
	@Test
	void testRemoveUserAddInfo() throws Exception {
		System.out.println(">> removed :"+userAddInfoService.removeUserAddInfo("user3"));
	}

}
