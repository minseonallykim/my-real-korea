package com.itwill.my_real_korea.dao.ticket;

import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.mapper.TicketImgMapper;
import com.itwill.my_real_korea.mapper.TicketMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@MapperScan(basePackageClasses = TicketImgMapper.class)
class TicketImgDaoImplTest{

    @Autowired
    private TicketImgDao ticketImgDao;

    @Test
    void insertTicketImg() throws Exception{
        int rowCount = ticketImgDao.insertTicketImg
                (new TicketImg(0,"test_img.jpg",1));
        assertEquals(rowCount,1);
    }

    @Test
    void selectTicketImgList() throws Exception {
        List<TicketImg> ticketImgList = ticketImgDao.selectTicketImgList(1);
        assertEquals(ticketImgList.size(), 1);
    }
    @Test
    void updateTicketImg() throws Exception{
        int rowCount = ticketImgDao.updateTicketImg(
                new TicketImg(3,"TESTTTT",0)
        );
        assertEquals(rowCount,1);
    }
    @Test
    void deleteTicketImg() throws Exception{
        int rowCount = ticketImgDao.deleteTicketImg(1);
        assertEquals(rowCount,1);
    }

    //티켓 이미지 DAO 테스트 완료
}