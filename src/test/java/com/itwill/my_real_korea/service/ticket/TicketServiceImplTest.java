package com.itwill.my_real_korea.service.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.mapper.CityMapper;
import com.itwill.my_real_korea.mapper.TicketMapper;
import com.itwill.my_real_korea.service.city.CityService;
//@SpringBootTest
@ComponentScan(basePackages = {"com.itwill.my_real_korea"})
class TicketServiceImplTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CityService cityService;
    @Autowired
    private TicketImgService ticketImgService;
    
    @Disabled
    @Test
    void insertTicket() throws Exception{
        City city = cityService.findByCityNo(3);
        Ticket ticket = new Ticket (0,"서비스테스트용",null,555555,"서비스인포인포","서비스노티스노티스", 0, city);
        
        int rowCount = ticketService.insertTicket(ticket);
        
        TicketImg ticketImg1 = new TicketImg(0,"서비스사진1.jpg", ticket.getTiNo());
        TicketImg ticketImg2 = new TicketImg(0,"서비스사진2.jpg", ticket.getTiNo());
        TicketImg ticketImg3 = new TicketImg(0,"서비스사진3.jpg", ticket.getTiNo());
        ticketImgService.insertTicketImg(ticketImg1);
        ticketImgService.insertTicketImg(ticketImg2);
        ticketImgService.insertTicketImg(ticketImg3);
        assertEquals(rowCount, 1);
    }
    @Test
    void selectAllTicket() throws Exception{
       System.out.println(ticketService.selectAllTicket(1));
    }

    @Test
    void selectByTicketAllSort() throws Exception{
        System.out.println(ticketService.selectByTicketAllSort(1,null,0,null));
    }
    //@Disabled
    @Test
    void selectByTicketNoCityWithImg() throws Exception{
        //assertNotNull(ticketService.selectByTicketNoCityWithImg(1));
        System.out.print(ticketService.selectByTicketNoCityWithImg(1));
    }
    @Disabled 
    @Test
    void updateTicket() throws Exception{
    	City city = cityService.findByCityNo(4);
    	assertEquals (ticketService.updateTicket(
                new Ticket(11,"수정이지롱",null,3333,"인포","어어어",0,city)),1);
    }
    @Disabled
    @Test
    void deleteTicket() throws Exception{
	   int rowCount = ticketService.deleteTicket(10);
	   assertEquals(rowCount, 1);
    }
}