package com.itwill.my_real_korea.dao.payment;

import com.itwill.my_real_korea.dao.ticket.TicketDao;
import com.itwill.my_real_korea.dao.ticket.TicketImgDao;
import com.itwill.my_real_korea.dao.tour.TourDao;
import com.itwill.my_real_korea.dao.tour.TourImgDao;
import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.Payment;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.tour.Tour;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class PaymentDaoImplTest {

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    TicketDao ticketDao;

    @Autowired
    TourDao tourDao;
    
    @Disabled
    @Test
    void testInsertPayment() throws Exception{
    	Ticket ticket=ticketDao.selectTicketNo(1);
		int rowCount1=paymentDao.insertTicketPayment(new Payment(0, 530000, 2, null, new Date(), "어? 금지", 530, 3,null,ticket,"user1"));
		Tour tour=tourDao.findTourWithCityByToNo(2);
		int rowCount2=paymentDao.insertTourPayment(new Payment(0, 59000, 1, null, new Date(), "집", 59, 1, tour, null, "user2"));

		assertEquals(rowCount1,1);
		assertEquals(rowCount2,1);
	}

    @Test
    void selectAllUser() throws Exception{
        List<Payment> paymentList = paymentDao.selectAllUser("qqqq1111");
        System.out.println(paymentList);
    }
    @Disabled
    @Test
    void selectPaymentNo() {
       // List<Payment> paymentList = paymentDao.selectPaymentNo(1);
        //System.out.println(paymentList);

        // System.out.println(paymentList);
    }
    @Disabled
    @Test
    void deletePayment() {
    }
}