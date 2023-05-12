package com.itwill.my_real_korea.dao.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.my_real_korea.dto.user.UserAddInfo;

//@Transactional
//@SpringBootTest
class UserAddInfoDaoImplTest {
	@Autowired
	private UserAddInfoDao userAddInfoDao; 
	
	@Test
	void init() {
		System.out.println(userAddInfoDao);
	}

	//1. 회원 추가 정보 등록
	@Test
	void testCreateUserAddInfo() throws Exception {
		UserAddInfo newAddInfo = new UserAddInfo("테스트", 0, 0, "user1");
		System.out.println(">> created :"+userAddInfoDao.createUserAddInfo(newAddInfo));
	}

	//2. 회원 추가 정보 보기
	@Test
	void testFindUserAddInfo() throws Exception {
		System.out.println(">> find :"+userAddInfoDao.findUserAddInfo("user1"));
	}

	//3. 회원 추가 정보 수정
	@Test
	void testUpdateUserAddInfo() throws Exception {
		UserAddInfo updateAddInfo = userAddInfoDao.findUserAddInfo("user2");
		updateAddInfo.setIntroduce("msg2");
		updateAddInfo.setAlcohol(0);
		updateAddInfo.setSmoking(0);
		System.out.println(">> updated :"+userAddInfoDao.updateUserAddInfo(updateAddInfo));
	}

	//4. 회원 추가 정보 삭제
	@Test
	void testRemoveUserAddInfo() throws Exception {
		System.out.println(">> removed :"+userAddInfoDao.removeUserAddInfo("user3"));
	}


}
