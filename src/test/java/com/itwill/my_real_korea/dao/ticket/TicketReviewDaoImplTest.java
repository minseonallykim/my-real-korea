package com.itwill.my_real_korea.dao.ticket;

import com.itwill.my_real_korea.dto.ticket.TicketReview;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.mapper.TicketReviewMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class TicketReviewDaoImplTest {

    @Autowired
    private TicketReviewDao ticketReviewDao;

 /*  @Test
    void insertTicketReview() {
        int rowCount = ticketReviewDao.insertTicketReview
                (new TicketReview(0,null,
                        "테스트","테스트내용","testReview.jpg",1,
                        new User("user3","0000",null,null,null,null,null,null,1,1,0,0,0)));
        assertEquals(rowCount,1);
    }*/
/*
    @Test
    void selectByTicketReview() {
        List<TicketReview> ticketReviewList
                = ticketReviewDao.selectByTicketReview(1,10,1);
        System.out.println(ticketReviewList);
    }

@Test
    void selectByTicketReviewUser() {
        List<TicketReview> ticketReviewUserList
                = ticketReviewDao.selectByTicketReviewUser(1,10,"user1");
        System.out.println(ticketReviewUserList);
    }
*/
  /*  @Test
    void updateTicketReview() {
        int rowCount
                = ticketReviewDao.updateTicketReview
                (new TicketReview(1,null,"수정테스트","테스트내용","modify.jpg",0,null));
        assertEquals(rowCount,1);
    }*/

    @Test
    void deleteTicketReview() {
        int rowCount = ticketReviewDao.deleteTicketReview(1);
        assertEquals(rowCount,1);
    }

    //티켓 리뷰 DAO 완료
}