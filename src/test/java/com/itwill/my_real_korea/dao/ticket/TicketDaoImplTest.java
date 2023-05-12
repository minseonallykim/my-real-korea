package com.itwill.my_real_korea.dao.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.Ticket;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TicketDaoImplTest {
	
	@Autowired
	private TicketDao ticketDao;

	//@Test
	void insertTicket() throws Exception {
		int rowCount = ticketDao.insertTicket
				(new Ticket(0, "인서트", null, 1111, "인포", "유의사항", 0,
	                        new City(1, null, 0, 0)));
	        assertEquals(rowCount, 1);
	        System.out.println(rowCount);
	        
	    }
	// 전체 목록 + 정렬 - 페이징
    @Test
    void selectAllTicket() throws Exception {
        List<Ticket> ticketList = ticketDao.selectAllTicket(1, 10/*,"tiPriDESC"*/);
        System.out.println(ticketList);
    }
    // 키워드, 지역, 가격 + 전체 LIST - 페이징 처리
    //@Test
    void selectByTicketAllSort() throws Exception {
    	//
        List<Ticket> ticketList = ticketDao.selectByTicketAllSort(1,10,null,0,"DESC");
        System.out.println(ticketList);
    }
    //상품 상세보기 - 지역 + 사진
  //  @Test
    void selectByTicketNoCityWithImg() throws Exception {
    	List<Ticket> ticketList = ticketDao.selectByTicketNoCityWithImg(1);
    	Ticket ticket = ticketList.get(0);
    	for(int i=1; i<ticketList.size(); i++) {
    		ticket.getTicketImgList().add(ticketList.get(i).getTicketImgList().get(0));
    	}
    	System.out.println(ticket);
    	
    }
    	
    	
	

}
