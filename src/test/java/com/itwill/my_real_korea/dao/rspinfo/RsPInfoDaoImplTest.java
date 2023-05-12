package com.itwill.my_real_korea.dao.rspinfo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.RsPInfo;


//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class RsPInfoDaoImplTest {

	@Autowired
	RsPInfoDao rsPInfo;
	
	void testInsertReservePerson() {
		RsPInfo reservePersonInfo1=new RsPInfo(0, "정유나1","jyn1@gmail" , "010-112", "user1", 1); 
		RsPInfo reservePersonInfo2=new RsPInfo(0, "정유나2","jyn2@gmail" , "010-112", "user1", 1); 
		RsPInfo reservePersonInfo3=new RsPInfo(0, "정유나3","jyn3@gmail" , "010-112", "user1", 1); 
		
		rsPInfo.insertRsPerson(reservePersonInfo1);
		rsPInfo.insertRsPerson(reservePersonInfo2);
		rsPInfo.insertRsPerson(reservePersonInfo3);
	}
	
	@Test
	void testSelectRsPersonByPNo() {
		//List<RsPInfo> rsPInfoList= rsPInfo.selectRsPersonByPNo(1);
		//System.out.println(rsPInfoList);
	}

}
