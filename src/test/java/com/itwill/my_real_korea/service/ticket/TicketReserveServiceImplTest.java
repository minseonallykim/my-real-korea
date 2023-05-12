package com.itwill.my_real_korea.service.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketReserve;

//@SpringBootTest
@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TicketReserveServiceImplTest {

	@Autowired
	TicketReserveService ticketReserveService;
	@Autowired
	TicketService ticketService;
	
	@Disabled
	@Test
	void testInsertTicketReserve() throws Exception {
		List <Ticket> ticketList = ticketService.selectByTicketNoCityWithImg(3);
		System.out.println(">>>>>>>>>>"+ticketList);
		Ticket ticket = ticketList.get(0);
		System.out.println(ticketReserveService.insertTicketReserve(
				new TicketReserve(0, new Date(), 5, "테스트중중중중중 ", ticket, "user3")));
	}
	@Test
	void testSelectByTicketReserveUser() throws Exception{
		List<TicketReserve> tickeList = ticketReserveService.selectByTicketReserveUser("user1");
		System.out.println(tickeList);
		
		//System.out.println(ticketReserveService.selectByTicketReserveUser("user1"));
		
	}
	@Disabled
	@Test
	void testSelectByTicketReserveNo() {
		fail("Not yet implemented");
	}

	@Disabled
	@Test
	void testDeleteTicketReserve() throws Exception{
		assertEquals(ticketReserveService.deleteTicketReserve(5),1);
	}

}
