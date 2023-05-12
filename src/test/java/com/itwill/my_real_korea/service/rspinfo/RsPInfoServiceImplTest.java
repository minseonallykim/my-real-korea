package com.itwill.my_real_korea.service.rspinfo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.RsPInfo;
//@SpringBootTest
@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class RsPInfoServiceImplTest {

	@Autowired
	private RsPInfoService rsPInfoService;
	
	void testInsertRsPerson() {
		RsPInfo reservePersonInfo1=new RsPInfo(0, "정유다1","jyn1@gmail" , "010-112", "user2", 1); 
		RsPInfo reservePersonInfo2=new RsPInfo(0, "정유다2","jyn2@gmail" , "010-112", "user2", 1); 
		RsPInfo reservePersonInfo3=new RsPInfo(0, "정유다3","jyn3@gmail" , "010-112", "user2", 1); 
		
		rsPInfoService.insertRsPerson(reservePersonInfo1);
		rsPInfoService.insertRsPerson(reservePersonInfo1);
		rsPInfoService.insertRsPerson(reservePersonInfo1);
	}

	@Test
	void testSelectRsPersonByPNo() {
		//List<RsPInfo> rsPInfoList=rsPInfoService.selectRsPersonByPNo(1);
		//System.out.println(rsPInfoList);
	}

}
