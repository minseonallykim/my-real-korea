package com.itwill.my_real_korea.controller;


import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.ticket.TicketReview;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.ticket.TicketReviewServiceImpl;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TicketService ticketService;
    @MockBean
    TicketImgService ticketImgService;
    @Autowired
    private TicketReviewServiceImpl ticketReviewService;

    @Test
    void ticketList() throws Exception {

        PageMakerDto<Ticket> ticketPage = new PageMakerDto<>();
        List<Ticket> ticketList = new ArrayList<>();
        List<TicketImg> ticketImgList = new ArrayList<>();

        // ticket 객체 생성
        Ticket ticket = new Ticket(1, null, null, 0, null, null, 0, null);
        // ticketImg 객체 생성 후 List에 담기
        ticketImgList.add(new TicketImg(1,null,1));
        // ticketImgList를 ticket 객체의 ticketImgList 필드에 담기 (setter 메소드)
        ticket.setTicketImgList(ticketImgList);
        // ticketImgList가 담긴 ticket 객체를 ticketList에 추가
        ticketList.add(ticket);
        ticketPage.setItemList(ticketList);

        ticketPage.setPageMaker(new PageMaker(1, 10));
        ticketPage.setTotRecordCount(4);

        given(ticketService.selectAllTicket(1)).willReturn(ticketPage); // 1페이지
        mockMvc.perform(get("/ticket-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-list"))
                .andDo(print());

        verify(ticketService).selectAllTicket(1);
    }


}