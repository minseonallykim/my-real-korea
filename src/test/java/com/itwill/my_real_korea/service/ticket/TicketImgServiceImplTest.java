package com.itwill.my_real_korea.service.ticket;

import static org.junit.jupiter.api.Assertions.*;

import com.itwill.my_real_korea.dto.ticket.TicketImg;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

//@SpringBootTest
@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TicketImgServiceImplTest {

	@Autowired
	TicketImgService ticketImgService;
	
	@Disabled
	@Test
	void testInsertTicketImg() throws Exception{
		assertEquals(ticketImgService.insertTicketImg(new TicketImg(0,"테스트",3)),1);
	}

	@Disabled
	@Test
	void testSelectTicketImgList() {
		List<TicketImg> ticketImgList = ticketImgService.selectTicketImgList(3);
		System.out.println(ticketImgList);
	}

	@Disabled
	@Test
	void testUpdateTicketImg() {
		List<TicketImg> ticketImgList = ticketImgService.selectTicketImgList(3);
		System.out.println(ticketImgList);
	}

	@Disabled
	@Test
	void testDeleteTicketImg() {
		assertEquals(ticketImgService.deleteTicketImg(4),1);
	}

}
