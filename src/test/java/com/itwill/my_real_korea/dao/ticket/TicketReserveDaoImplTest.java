package com.itwill.my_real_korea.dao.ticket;

import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketReserve;
import com.itwill.my_real_korea.mapper.TicketReserveMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@MapperScan(basePackageClasses = TicketReserveMapper.class)
class TicketReserveDaoImplTest {

    @Autowired
    TicketReserveDao ticketReserveDao;

    @Disabled
    @Test
    void insertTicketReserve() throws Exception{
        int rowCount = ticketReserveDao.insertTicketReserve(new TicketReserve(
                0,new Date(),1,"요청사항",
                        new Ticket(1,null,null,0,null,null,0,null),"user1"));
        assertEquals(rowCount,1);
        System.out.print("Test>>>>"+rowCount);
    }
    @Disabled
    @Test
    void selectByTicketReserveUser() throws Exception{
        List<TicketReserve> ticketReserveList =
                ticketReserveDao.selectByTicketReserveUser("user1");
        System.out.print(ticketReserveList);

    }
    @Disabled
    @Test
    void selectByTicketReserveNo()  throws Exception {
        List<TicketReserve> ticketReserveNoList
                = ticketReserveDao.selectByTicketReserveNo(1);
        System.out.print(ticketReserveNoList);
    }
    @Disabled
    @Test
    void deleteTicketReserve() throws Exception{
        int rowCount = ticketReserveDao.deleteTicketReserve(1);
        assertEquals(rowCount,1);
    }

    //티켓 예약 Dao 테스트 완료
}