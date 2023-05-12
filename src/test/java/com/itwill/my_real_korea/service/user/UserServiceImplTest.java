package com.itwill.my_real_korea.service.user;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.my_real_korea.dto.user.User;

//@Transactional
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class UserServiceImplTest {
	
	@Autowired
	private UserService userService;
	
	//1. 회원 가입
	//@Test
	void testCreate() throws Exception {
		User user = new User("serviceTest1", "test1111", "테스트", "테스트", "011-1111-1111", "test1@gmail.com", new Date(), "테스트", 0, 0, 1, 0, 0);
		System.out.println(">> created :"+userService.create(user));
	}
	
	//2. 회원 정보 보기 (마이페이지)
	//@Test
	void testFindUser() throws Exception {
		System.out.println(">> findUser :"+userService.findUser("user1"));
	}
	
	//3. 전체 회원 정보 보기 (관리자 페이지)
	@Test
	void testFindUserList() throws Exception {
		System.out.println(">> findUserList :"+userService.findUserList());
	}
	
	//4. 회원 정보 수정
	@Test
	void testUpdate() throws Exception {
		User user = userService.findUser("user4");
		user.setPassword("user4444");
		System.out.println(">> updated :"+userService.update(user));
	}
	
	//5. 회원 탈퇴
	//@Test
	void testRemove() throws Exception {
		int removeRowCount = userService.remove("user1");
		System.out.println(">> removed rowCount : "+removeRowCount);
	}
	
	
	//11. 아이디 중복 체크 (회원 가입(회원 존재 여부 확인))
	//@Test
	void testIsDuplicateId() throws Exception {
		System.out.println("is Duplicated : "+userService.countExistId("user1"));
		System.out.println("is Duplicated : "+userService.countExistId("user11"));
	}
	
	//12. 로그인 (비밀번호 일치 여부 확인)
	//@Test
	void testLogin() throws Exception {
		System.out.println("user login : "+userService.login("admin", "admin0000"));
	}
	
	//13. 이메일, 이름으로 아이디 찾기
	//@Test
	void testFindId() throws Exception {
		System.out.println(">> ID : "+userService.findIdByEmailName("kgee12300@gmail.com","이름"));
	}
	
	//14. 비밀번호 찾기 (일치하는 회원 존재 여부 확인)
	//@Test
	void findIdByIdEmail() throws Exception {
		System.out.println(">> matchCount : "+userService.findIdByIdEmail("user1", "user1@gmail.com"));
	}
	
	//15. 비밀번호 찾기 (비밀번호 재설정, 임시 비밀번호 발송)
	//@Test
	void sendTempPassword() throws Exception {
		userService.sendTempPassword("test11", "kgee12300@gmail.com");
	}
	
	
	//21. 메일 인증여부 확인
	//@Test
	void mailAuth() throws Exception {
		System.out.println(">> mailAuth : "+userService.findMailAuth("user1"));
	}
	
	//22. 메일 인증번호 업데이트
	//@Test
	void mailKeyUpdate() throws Exception {
		User user3 = userService.findUser("user1");
		user3.setMailKey(1234);
		System.out.println(">> mailKeyUpdate : "+userService.updateMailKey(user3));
	}
	
	//23. 메일 인증여부 업데이트
	//@Test
	void mailAuthUpdate() throws Exception {
		User user3 = userService.findUser("user1");
		user3.setMailAuth(1);
		System.out.println(">> mailAuthUpdate : "+userService.updateMailAuth(user3));
	}
	
}

