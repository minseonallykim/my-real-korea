package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itwill.my_real_korea.service.ticket.TicketReviewService;
import org.springframework.web.bind.annotation.*;

import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.service.ticket.TicketImgService;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiOperation;

//@RestController
public class TicketRestController {

    private final TicketService ticketService;
    private final TicketImgService ticketImgService;
    private final TicketReviewService ticketReviewService;

    //@Autowired
    public TicketRestController(TicketService ticketService, TicketImgService ticketImgService,
                                TicketReviewService ticketReviewService) {
        this.ticketService = ticketService;
        this.ticketImgService = ticketImgService;
        this.ticketReviewService = ticketReviewService;
    }

    @PostMapping(value = "/ticket-list-sort", produces = "application/json;charset=UTF-8")
    public Map<String, Object> ticketList(@RequestBody Map<String,String> map){
        Map<String, Object> ticketMap = new HashMap<>();
        int code = 1;
        String msg = "성공";
        PageMakerDto<Ticket> data = null;
        int currentPage = Integer.parseInt(map.get("currentPage"));
        int cityNo = Integer.parseInt(map.get("cityNo"));
        String keyword = map.get("keyword");
        String sortOrder = map.get("sortOrder");

        try {
            PageMakerDto<Ticket> ticketPage
                    = ticketService.selectByTicketAllSort(currentPage,keyword, cityNo, sortOrder);
            data = ticketPage;
            code = 1;
            msg = "성공";
        } catch (Exception e){
            e.printStackTrace();
            code = 2;
            msg = "관리자에게 문의하세요.";
        }
        ticketMap.put("code", code);
        ticketMap.put("msg", msg);
        ticketMap.put("data", data);
        return ticketMap;
    }

}
